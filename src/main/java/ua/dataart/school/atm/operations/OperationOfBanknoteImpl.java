package ua.dataart.school.atm.operations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;
import ua.dataart.school.atm.operations.behavior.OperationOfBanknote;
import ua.dataart.school.atm.storage.BanknoteStorage;

public class OperationOfBanknoteImpl implements OperationOfBanknote{
	private static final Logger LOG = Logger.getLogger(OperationOfBanknoteImpl.class);
	private static InputStream inputStream;
	private BanknoteStorage storageOfBanknotes;
	private List<Banknote> cloneStorageOfBanknotes;
	// TODO: 8/11/16 eugene - bad name for field
	private int maxCountOfBanknote;
	// TODO: 8/11/16 eugene - bad name for field
	private int fixedCountGivenOfBanknotes;
	// TODO: 8/11/16 eugene - bad name for field
	private List<Banknote> savedStorage;
	private int requiredAmount;
	// TODO: 8/11/16 eugene - bad name for field
	private int savedCurrentAmount;
	// TODO: 8/11/16 eugene - bad name for field
	private int amountGivenOfBanknotes;
	// TODO: 8/11/16 eugene - bad name for field
	private int inputCountOfBanknote;
	private int availableCountOfBanknote;
	private int currentCountOfBanknote;
	private int currentValueOfBanknote;
	private int flagForMethodWorkWithBanknote;
	
	public OperationOfBanknoteImpl() throws IOException{
		ConfigurationOfProperty instanceConfiguration=ConfigurationOfProperty.getInstance();
		int capacity=instanceConfiguration.getArrayValuesOfProperty("value").length;
		int arrayRasultValues[]=instanceConfiguration.getArrayValuesOfProperty("value");
		int arrayResultCounts[]=instanceConfiguration.getArrayValuesOfProperty("count");
		maxCountOfBanknote=instanceConfiguration.getValueOfPropertyWithName("maxCount");
		fixedCountGivenOfBanknotes=instanceConfiguration.getValueOfPropertyWithName("fixedCountGiven");
		storageOfBanknotes=new BanknoteStorage(capacity, arrayRasultValues, arrayResultCounts);
	}

	public static InputStream getInputStream() {
		return inputStream;
	}
	
	@Override
	public int giveRequiredCash(BanknoteStorage inputStorageOfBanknotes) throws IOException, CloneNotSupportedException {
		initPrimaryValues();
		//field flagForMethodWorkWithBanknote initialize positive value for work method workWithBanknotesForMethodsGiveRequiredCashAndAcceptInputCash
		flagForMethodWorkWithBanknote = 1;
		workWithBanknotesForMethodsGiveRequiredCashAndAcceptInputCash(inputStorageOfBanknotes);

		return savedCurrentAmount;
	}
	
	@Override
	public int acceptInputCash(BanknoteStorage inputStorageOfBanknotes) throws IOException, CloneNotSupportedException {
		initPrimaryValues();
		//field flagForMethodWorkWithBanknote initialize negative value for work method workWithBanknotesForMethodsGiveRequiredCashAndAcceptInputCash
		flagForMethodWorkWithBanknote = -1;
		workWithBanknotesForMethodsGiveRequiredCashAndAcceptInputCash(inputStorageOfBanknotes);

		return savedCurrentAmount;
	}

	@Override
	public int giveRemainingAmountOfCash() {
		if (amountGivenOfBanknotes >= fixedCountGivenOfBanknotes) {
			return savedCurrentAmount;
		}
		int resultOfCountForRequiredAmount = 0;
		for (int index = 0; index < cloneStorageOfBanknotes.size(); index++) {
			int missingAmount = requiredAmount - savedCurrentAmount;
			LOG.info("missingAmount=" + missingAmount);
			Banknote currentBanknote = cloneStorageOfBanknotes.get(index);
			Banknote currentBanknoteInSavedStorage = savedStorage.get(index);
			if (currentBanknote.getCount() != 0 && missingAmount >= currentBanknote.getValue()) {
				currentCountOfBanknote = currentBanknote.getCount();
				currentValueOfBanknote = currentBanknote.getValue();
				resultOfCountForRequiredAmount = missingAmount / currentValueOfBanknote;
				availableCountOfBanknote = fixedCountGivenOfBanknotes - amountGivenOfBanknotes;
				saveResultInStorageForGiveRemainingAmountOfCash(resultOfCountForRequiredAmount,
						currentBanknoteInSavedStorage, currentBanknote);
			}
			if (requiredAmount == savedCurrentAmount || amountGivenOfBanknotes >= fixedCountGivenOfBanknotes) {
				break;
			}
		}
		return savedCurrentAmount;
	}
	
	private void saveResultInStorageForGiveRemainingAmountOfCash(int numberOfBanknotesForShortfallAmount, Banknote banknoteInSavedStorage, Banknote currentBanknote){
		if (numberOfBanknotesForShortfallAmount > currentCountOfBanknote) {
			numberOfBanknotesForShortfallAmount = currentCountOfBanknote;
		}
		if (numberOfBanknotesForShortfallAmount > availableCountOfBanknote) {
			numberOfBanknotesForShortfallAmount = availableCountOfBanknote;
		}
		savedCurrentAmount += numberOfBanknotesForShortfallAmount * currentValueOfBanknote;
		banknoteInSavedStorage.setValue(currentValueOfBanknote);
		banknoteInSavedStorage.setCount(numberOfBanknotesForShortfallAmount);
		amountGivenOfBanknotes += numberOfBanknotesForShortfallAmount;
		currentCountOfBanknote -= numberOfBanknotesForShortfallAmount;
		currentBanknote.setCount(currentCountOfBanknote);
	}
	
	private void workWithBanknotesForMethodsGiveRequiredCashAndAcceptInputCash(BanknoteStorage inputStorageOfBanknotes) {
		requiredAmount=getAmountFromInputStorageOfBanknotes(inputStorageOfBanknotes.getBanknotes());
		for (int index = 0; index < cloneStorageOfBanknotes.size(); index++) {
			Banknote inputBanknote = inputStorageOfBanknotes.getBanknotes().get(index);
			Banknote currentBanknote = cloneStorageOfBanknotes.get(index);
			Banknote currentBanknoteInSavedStorage = savedStorage.get(index);
			if (inputBanknote.getValue() == currentBanknote.getValue() && inputBanknote.getCount() != 0) {
				currentCountOfBanknote = currentBanknote.getCount();
				currentValueOfBanknote = currentBanknote.getValue();
				inputCountOfBanknote = inputBanknote.getCount();
				availableCountOfBanknote = fixedCountGivenOfBanknotes - amountGivenOfBanknotes;
				if (flagForMethodWorkWithBanknote < 0) {
					saveResultInStorageForAcceptInputCash(currentBanknote, currentBanknoteInSavedStorage);
				} else if(flagForMethodWorkWithBanknote > 0) {
					saveResultInStorageForGiveRequiredCash(currentBanknote, currentBanknoteInSavedStorage);					
				}
			}
			if (requiredAmount == savedCurrentAmount || amountGivenOfBanknotes >= fixedCountGivenOfBanknotes) {
				break;
			}
		}
	}

	private void saveResultInStorageForGiveRequiredCash(Banknote currentBanknote, Banknote savedBanknoteInSavedStorage) {
		if (inputCountOfBanknote > currentCountOfBanknote) {
			inputCountOfBanknote = currentCountOfBanknote;
		}
		if (inputCountOfBanknote > availableCountOfBanknote) {
			inputCountOfBanknote = availableCountOfBanknote;
		}
		savedCurrentAmount += inputCountOfBanknote * currentValueOfBanknote;
		savedBanknoteInSavedStorage.setValue(currentValueOfBanknote);
		savedBanknoteInSavedStorage.setCount(inputCountOfBanknote);
		amountGivenOfBanknotes += inputCountOfBanknote;
		currentCountOfBanknote -= inputCountOfBanknote;
		currentBanknote.setCount(currentCountOfBanknote);
	}
	
	private void saveResultInStorageForAcceptInputCash(Banknote currentBanknote, Banknote savedBanknoteInSavedStorage) {
		if (maxCountOfBanknote > currentCountOfBanknote) {
			if ((maxCountOfBanknote - currentCountOfBanknote) < inputCountOfBanknote) {
				inputCountOfBanknote = maxCountOfBanknote - currentCountOfBanknote;
			}
			savedCurrentAmount += inputCountOfBanknote * currentValueOfBanknote;
			savedBanknoteInSavedStorage.setValue(currentValueOfBanknote);
			savedBanknoteInSavedStorage.setCount(inputCountOfBanknote);
			currentCountOfBanknote += inputCountOfBanknote;
			currentBanknote.setCount(currentCountOfBanknote);
		}
	}
	
	private void initPrimaryValues() throws CloneNotSupportedException{
		cloningStorageOfBanknotes();
		savedStorage=new BanknoteStorage().getBanknotes();
		savedCurrentAmount=0;
		amountGivenOfBanknotes=0;
	}
	
	public int getAmountFromInputStorageOfBanknotes(List<Banknote> storage) {
		int requiredAmountFromInputBanknotes=0;
		for (Banknote banknote : storage) {
			if(banknote.getCount()!=0){
				requiredAmountFromInputBanknotes+=banknote.getValue()*banknote.getCount();
			}
		}
		return requiredAmountFromInputBanknotes;
	}

	public List<Banknote> getSaveStorage() {
		return savedStorage;
	}
	
	public BanknoteStorage getCopyOfTheStorageOfBanknotes() throws CloneNotSupportedException{
		BanknoteStorage copyBanknoteStorage=new BanknoteStorage();
		List<Banknote> listBanknotes=new ArrayList<>();
		for(Banknote banknote: storageOfBanknotes.getBanknotes()){
			listBanknotes.add(banknote.clone());
		}
		copyBanknoteStorage.setBanknotes(listBanknotes);
		return copyBanknoteStorage;
	}

	public void	saveCurrentStorageInMemory() throws CloneNotSupportedException{
		storageOfBanknotes.setBanknotes(cloneStorageOfBanknotes);
	}
	
	private void cloningStorageOfBanknotes() throws CloneNotSupportedException {
		cloneStorageOfBanknotes=new ArrayList<Banknote>(storageOfBanknotes.getBanknotes().size());
		for(Banknote banknote: storageOfBanknotes.getBanknotes()){
			cloneStorageOfBanknotes.add(banknote.clone());
		}
	}
}
