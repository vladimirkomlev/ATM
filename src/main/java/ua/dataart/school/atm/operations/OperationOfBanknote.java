package ua.dataart.school.atm.operations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;
import ua.dataart.school.atm.storage.BanknoteStorage;

// TODO: 8/11/16 eugene - bad formatting
// TODO: 8/11/16 eugene - bad name for class. It's not informative at all. And no plural form allowed
//vova - Operations renamed on OperationOnBanknote
public class OperationOfBanknote{
	private static final Logger LOG = Logger.getLogger(OperationOfBanknote.class);
	private static InputStream inputStream;
	// TODO: 8/11/16 eugene - no need in keeping Properties in memory. You need them only during instantiation
	private Properties properties;
	// TODO: 8/11/16 eugene - some of fields below should be final
	// TODO: 8/11/16 eugene - bad name for field. Its not current, it never changes
	private BanknoteStorage banknoteStorageCurrent;
	// TODO: 8/11/16 eugene - bad name for field
	private List<Banknote> banknoteStorageFromCurrentStorage;
	// TODO: 8/11/16 eugene - bad name for field
	private int maxCountOfValueFromFile;
	// TODO: 8/11/16 eugene - bad name for field
	private int fixedCountGivenOfBanknotes;
	// TODO: 8/11/16 eugene - bad name for field
	private List<Banknote> saveStorage;
	private int requiredAmount;
	// TODO: 8/11/16 eugene - bad name for field
	private int saveCurrentAmount;
	// TODO: 8/11/16 eugene - bad name for field
	private int amountGivenOfBanknotes;
	// TODO: 8/11/16 eugene - bad name for field
	private int countHelper;
	// TODO: 8/11/16 eugene - bad name for field
	private int inputCountOfBanknote;
	private int availableCountOfBanknote;
	private int currentCountOfBanknote;
	private int currentValueOfBanknote;

	public OperationOfBanknote(InputStream inStream) {
		try {
			OperationOfBanknote.inputStream = inStream;
			properties=new Properties();
			properties.load(inputStream);
			banknoteStorageCurrent=new BanknoteStorage();
			// TODO: 8/11/16 eugene - this is really strange construction
			// TODO: 8/11/16 eugene - for getting banknote list you create another BanknoteStorage
			banknoteStorageCurrent.setBanknotes(getBanknoteStorageFromFile());
			maxCountOfValueFromFile=getValueOfNameFromFile("maxCount");
			fixedCountGivenOfBanknotes=getValueOfNameFromFile("fixedCountGiven");
		} catch (IOException e) {
			// TODO: 8/11/16 eugene - is it really normal to continue working if your key instance broke down?
			LOG.info("File not found or damaged. " + e.fillInStackTrace());
		}

	}

	public static InputStream getInputStream() {
		return inputStream;
	}

	// TODO: 8/11/16 eugene - overcomplicated method. Rework and split into separate methods
	// TODO: 8/11/16 eugene - 'get' means this is getter-method. Rename or make it getter
	// TODO: 8/11/16 eugene - not informative argument name
	public int getCash(BanknoteStorage banknoteStorageInput) throws IOException, CloneNotSupportedException {
		banknoteStorageFromCurrentStorage=getCopyStorageBanknote(banknoteStorageCurrent.getBanknotes());
		saveStorage=new BanknoteStorage().getBanknotes();
		saveCurrentAmount=0;
		amountGivenOfBanknotes=0;
		
		requiredAmount=getSumFromBanknoteStorageInput(banknoteStorageInput.getBanknotes());
		workWithBanknotesForMethodsGetCashAndPutCach(banknoteStorageInput);
//		for (int index=0;index < banknoteStorageFromCurrentStorage.size();index++) {
//			Banknote inputBanknote=banknoteStorageInput.getBanknotes().get(index);
//			Banknote currentBanknote=banknoteStorageFromCurrentStorage.get(index);
//			Banknote savedBanknoteInSaveStorage=saveStorage.get(index);
//			if(currentBanknote.getValue()==inputBanknote.getValue() && inputBanknote.getCount() != 0){
//					currentCountOfBanknote = currentBanknote.getCount();
//					currentValueOfBanknote = currentBanknote.getValue();
//					inputCountOfBanknote = inputBanknote.getCount();
//					savedBanknoteInSaveStorage.setValue(currentValueOfBanknote);
//					availableCountOfBanknote = fixedCountGivenOfBanknotes - amountGivenOfBanknotes;
//					// TODO: 8/11/16 eugene - too much 'if' statements complicate code
//					if (availableCountOfBanknote > 0) {
//						// TODO: 8/11/16 eugene - in both conditions code is almost the same. Extract it to some method
//						saveResultInStorage(currentBanknote, savedBanknoteInSaveStorage);
//					}
//			}
//			
//			if(requiredAmount==saveCurrentAmount || amountGivenOfBanknotes>=fixedCountGivenOfBanknotes){
//				break;
//			}
//		}
		
		return saveCurrentAmount;
	}

	// TODO: 8/11/16 eugene - this method looks like previous. Same method - same problems
	public int getCashWhenResultSumLessRequiredSum() {
		if(amountGivenOfBanknotes>=fixedCountGivenOfBanknotes){
			System.out.println("saveCurrentSum="+saveCurrentAmount);
			return saveCurrentAmount;
		}
		int missingAmount=requiredAmount-saveCurrentAmount;
		int resultValueOfBanknote=0;
		for(int index2=0;index2<banknoteStorageFromCurrentStorage.size();index2++){
			if(banknoteStorageFromCurrentStorage.get(index2).getCount()!=0){
				if(missingAmount>=banknoteStorageFromCurrentStorage.get(index2).getValue()){
					currentCountOfBanknote=banknoteStorageFromCurrentStorage.get(index2).getCount();
					currentValueOfBanknote=banknoteStorageFromCurrentStorage.get(index2).getValue();
					resultValueOfBanknote=missingAmount/currentValueOfBanknote;
					saveStorage.get(index2).setValue(currentValueOfBanknote);
					availableCountOfBanknote=fixedCountGivenOfBanknotes-amountGivenOfBanknotes;
					if(resultValueOfBanknote>currentCountOfBanknote){
						countHelper=currentCountOfBanknote;
						if(currentCountOfBanknote>availableCountOfBanknote){
							currentCountOfBanknote=availableCountOfBanknote;
						}
						saveCurrentAmount+=currentCountOfBanknote*currentValueOfBanknote;
						saveStorage.get(index2).setCount(currentCountOfBanknote);
						missingAmount=requiredAmount-saveCurrentAmount;
						amountGivenOfBanknotes+=currentCountOfBanknote;
						currentCountOfBanknote=countHelper-currentCountOfBanknote;
						banknoteStorageFromCurrentStorage.get(index2).setCount(currentCountOfBanknote);
					}else if(resultValueOfBanknote<=currentCountOfBanknote){
						if(resultValueOfBanknote>availableCountOfBanknote){
							resultValueOfBanknote=availableCountOfBanknote;
						}
						saveCurrentAmount+=resultValueOfBanknote*currentValueOfBanknote;
						saveStorage.get(index2).setCount(resultValueOfBanknote);
						amountGivenOfBanknotes+=resultValueOfBanknote;
						missingAmount=requiredAmount-saveCurrentAmount;
						currentCountOfBanknote-=resultValueOfBanknote;
						banknoteStorageFromCurrentStorage.get(index2).setCount(currentCountOfBanknote);
					}
				}
			}
			if(requiredAmount==saveCurrentAmount){
				System.out.println("requiredAmount="+saveCurrentAmount);
				return saveCurrentAmount;
			}
			if(amountGivenOfBanknotes>=fixedCountGivenOfBanknotes){
				return saveCurrentAmount;
			}
		}
		System.out.println("saveCurrentSum="+saveCurrentAmount);
		return saveCurrentAmount;
	}

	// TODO: 8/11/16 eugene - it all looks pretty much the same
	public int putCash(BanknoteStorage banknoteStorageInput) throws IOException, CloneNotSupportedException {
		banknoteStorageFromCurrentStorage=getCopyStorageBanknote(banknoteStorageCurrent.getBanknotes());
		saveStorage=new BanknoteStorage().getBanknotes();
		List<Banknote> storage=banknoteStorageFromCurrentStorage;
		int saveCurrentAmountPutCash=0;
		int requiredAmountPutCash;

		requiredAmountPutCash=getSumFromBanknoteStorageInput(banknoteStorageInput.getBanknotes());
		for(int index=0;index<storage.size();index++){
			Banknote inputBanknote=banknoteStorageInput.getBanknotes().get(index);
			if(storage.get(index).getValue()==inputBanknote.getValue() && inputBanknote.getCount() != 0){
				currentCountOfBanknote = storage.get(index).getCount();
				currentValueOfBanknote = storage.get(index).getValue();
				inputCountOfBanknote = inputBanknote.getCount();
				saveStorage.get(index).setValue(currentValueOfBanknote);

				if (maxCountOfValueFromFile > currentCountOfBanknote) {
					if ((maxCountOfValueFromFile - currentCountOfBanknote) < inputCountOfBanknote) {
						inputCountOfBanknote = maxCountOfValueFromFile - currentCountOfBanknote;
					}
					saveCurrentAmountPutCash += inputCountOfBanknote * currentValueOfBanknote;
					saveStorage.get(index).setCount(inputCountOfBanknote);
					currentCountOfBanknote += inputCountOfBanknote;
					storage.get(index).setCount(currentCountOfBanknote);
				}
			}
			if(requiredAmountPutCash==saveCurrentAmountPutCash){
				System.out.println("saveCurrentAmount="+saveCurrentAmountPutCash);
				break;
			}
		}

		return saveCurrentAmountPutCash;
	}
	
	

	public int getSumFromBanknoteStorageInput(List<Banknote> storage) {
		// TODO: 8/11/16 eugene - variable with the same name as field. That's really bad practice
		int requiredAmount=0;
		for (Banknote banknote : storage) {
			if(banknote.getCount()!=0){
				requiredAmount+=banknote.getValue()*banknote.getCount();
			}
		}
		return requiredAmount;
	}

	// TODO: 8/11/16 eugene - poor method design. Instead of argument create BanknoteStorage inside method
	// TODO: 8/11/16 eugene - Better receive Properties as an argument
	public List<Banknote> getBanknoteStorageFromFile() throws IOException {
		BanknoteStorage banknoteStorage = new BanknoteStorage();
		int resultValues[] = getArrayPropertiesBanknotes("value");
		int resultCounts[] = getArrayPropertiesBanknotes("count");
		int index = 0;
		// TODO: 8/11/16 eugene - here it would be better to iterate using indexed loop
		for (Banknote banknote : banknoteStorage.getBanknotes()) {
			banknote.setValue(resultValues[index]);
			banknote.setCount(resultCounts[index]);
			index++;
		}

		return banknoteStorage.getBanknotes();
	}

	// TODO: 8/11/16 eugene - Better receive Properties as an argument
	public int getValueOfNameFromFile(String inputValue) {
		// TODO: 8/11/16 eugene - you could do this in single line
		// TODO: 8/11/16 eugene - return Integer.parseInt(properties.getProperty(inputValue));
		int intResult;
		intResult=Integer.parseInt(properties.getProperty(inputValue));
		return intResult;
	}

	public List<Banknote> getSaveStorage() {
		return saveStorage;
	}

	public void	saveCurrentStorageInMemory() throws CloneNotSupportedException{
		banknoteStorageCurrent.setBanknotes(getCopyStorageBanknote(banknoteStorageFromCurrentStorage));
	}
	
	private void workWithBanknotesForMethodsGetCashAndPutCach(BanknoteStorage banknoteStorageInput){
		for(int index=0;index<banknoteStorageFromCurrentStorage.size();index++){
			Banknote inputBanknote=banknoteStorageInput.getBanknotes().get(index);
			Banknote currentBanknote=banknoteStorageFromCurrentStorage.get(index);
			Banknote savedBanknoteInSaveStorage=saveStorage.get(index);
			if(inputBanknote.getValue()==currentBanknote.getValue() && inputBanknote.getCount()!=0){
				currentCountOfBanknote=currentBanknote.getCount();
				currentValueOfBanknote=currentBanknote.getValue();
				inputCountOfBanknote=inputBanknote.getCount();
				savedBanknoteInSaveStorage.setValue(currentValueOfBanknote);
				availableCountOfBanknote = fixedCountGivenOfBanknotes - amountGivenOfBanknotes;
				if(availableCountOfBanknote>0){
					saveResultInStorage(currentBanknote, savedBanknoteInSaveStorage);
				}
			}
			if(requiredAmount==saveCurrentAmount || amountGivenOfBanknotes>=fixedCountGivenOfBanknotes){
				break;
			}
		}
	}
	
	private void saveResultInStorage(Banknote currentBanknote, Banknote savedBanknoteInStorage){
		if(inputCountOfBanknote>currentCountOfBanknote){
			inputCountOfBanknote=currentCountOfBanknote;
		}
		if(inputCountOfBanknote>availableCountOfBanknote){
			inputCountOfBanknote=availableCountOfBanknote;
		}
		saveCurrentAmount+=inputCountOfBanknote*currentValueOfBanknote;
		savedBanknoteInStorage.setCount(inputCountOfBanknote);
		amountGivenOfBanknotes+=inputCountOfBanknote;
		currentCountOfBanknote-=inputCountOfBanknote;
		currentBanknote.setCount(currentCountOfBanknote);
	}
	
	// TODO: 8/11/16 eugene - maybe 'cloneBanknotes' is a better name?
	private List<Banknote> getCopyStorageBanknote(List<Banknote> inputStorage) throws CloneNotSupportedException {
		// TODO: 8/11/16 eugene - starting from java 7 type in constructor call is redundant
		List<Banknote>cloneStorage=new ArrayList<Banknote>(inputStorage.size());
		for(Banknote banknote: inputStorage){
			cloneStorage.add(banknote.clone());
		}
		return cloneStorage;
	}

	private int[] getArrayPropertiesBanknotes(String value) throws IOException {
		// TODO: 8/11/16 eugene - this looks strange
		int intResults[] = new int[0];
		try {
			String strValues[] = properties.getProperty(value).split(";");
			intResults = new int[strValues.length];
			for (int i = 0; i < intResults.length; i++) {
				intResults[i] = Integer.valueOf(strValues[i]);
			}
		} catch (Exception e) {
			System.out.println("File not found or damaged.");
			LOG.info("File not found or damaged. " + e.fillInStackTrace());
		}
		return intResults;
	}

}
