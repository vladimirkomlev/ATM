package ua.dataart.school.atm.operations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;
import ua.dataart.school.atm.storage.BanknoteStorage;

public class Operations implements Cloneable{
	private static final Logger log = Logger.getLogger(Operations.class);
	private static InputStream filePath;
	private Properties properties;
	private BanknoteStorage banknoteStorageCurrent;
	private List<Banknote> banknoteStorageFromCurrentStorage;
	private int maxCountOfValueFromFile;
	private int fixedCountGivenBanknotes;
	private List<Banknote> saveStorage;
	private int requiredAmount;
	private int saveCurrentAmount;
	private int amountGivenCountOfBanknotes;
	private int countHelper;
	private int availableCurrentCountOfBanknote;
	private int currentCountOfBanknote;
	private int currentValueOfBanknote;

	public Operations(InputStream url) {
		try {
			Operations.filePath = url;
			properties=new Properties();
			properties.load(filePath);
//			filePath.close();
			banknoteStorageCurrent=new BanknoteStorage();
			banknoteStorageCurrent.setBanknotes(getBanknoteStorageFromFile(new BanknoteStorage()));
			maxCountOfValueFromFile=getValueOfNameFromFile("maxCount");
			fixedCountGivenBanknotes=getValueOfNameFromFile("fixedCountGiven");	
		} catch (IOException e) {
			log.info("File not found or damaged. " + e.fillInStackTrace());
		}
			
	}
	
	public static InputStream getFilePath() {
		return filePath;
	}
	
	public int getCash(BanknoteStorage banknoteStorageInput) throws IOException, CloneNotSupportedException {
		banknoteStorageFromCurrentStorage=getCopyStorageBanknote(banknoteStorageCurrent.getBanknotes());
		saveStorage=new BanknoteStorage().getBanknotes();
		saveCurrentAmount=0;
		amountGivenCountOfBanknotes = 0;
		countHelper=0;
		requiredAmount=0;
		availableCurrentCountOfBanknote=0;
		currentCountOfBanknote=0;
		currentValueOfBanknote=0;
		int inputCountOfBanknote=0;
		
		requiredAmount=getSumFromBanknoteStorageInput(banknoteStorageInput.getBanknotes());
		for (int index=0;index < banknoteStorageFromCurrentStorage.size();index++) {
			if(banknoteStorageFromCurrentStorage.get(index).getValue()==banknoteStorageInput.getBanknotes().get(index).getValue()){
				if (banknoteStorageInput.getBanknotes().get(index).getCount() != 0) {
					currentCountOfBanknote = banknoteStorageFromCurrentStorage.get(index).getCount();
					currentValueOfBanknote = banknoteStorageFromCurrentStorage.get(index).getValue();
					inputCountOfBanknote = banknoteStorageInput.getBanknotes().get(index).getCount();
					saveStorage.get(index).setValue(currentValueOfBanknote);
					availableCurrentCountOfBanknote = fixedCountGivenBanknotes - amountGivenCountOfBanknotes;
					if (availableCurrentCountOfBanknote > 0) {
						if (inputCountOfBanknote > currentCountOfBanknote) {
							countHelper = currentCountOfBanknote;
							if (currentCountOfBanknote > availableCurrentCountOfBanknote) {
								currentCountOfBanknote = availableCurrentCountOfBanknote;
							}
							saveCurrentAmount += currentCountOfBanknote * currentValueOfBanknote;
							saveStorage.get(index).setCount(currentCountOfBanknote);
							amountGivenCountOfBanknotes += currentCountOfBanknote;
							currentCountOfBanknote = countHelper - currentCountOfBanknote;
							banknoteStorageFromCurrentStorage.get(index).setCount(currentCountOfBanknote);
						} else if (inputCountOfBanknote <= currentCountOfBanknote) {
							if (inputCountOfBanknote > availableCurrentCountOfBanknote) {
								inputCountOfBanknote = availableCurrentCountOfBanknote;
							}
							saveCurrentAmount += inputCountOfBanknote * currentValueOfBanknote;
							saveStorage.get(index).setCount(inputCountOfBanknote);
							amountGivenCountOfBanknotes += inputCountOfBanknote;
							currentCountOfBanknote -= inputCountOfBanknote;
							banknoteStorageFromCurrentStorage.get(index).setCount(currentCountOfBanknote);
						}
					}
				}
			}
			if(requiredAmount==saveCurrentAmount){
				return saveCurrentAmount;
			}
			if(amountGivenCountOfBanknotes>=fixedCountGivenBanknotes){
				return saveCurrentAmount;
			}
		}
		
		System.out.println("saveCurrentSum="+saveCurrentAmount);
		return saveCurrentAmount;
	}
	
	public int getCashWhenResultSumLessRequiredSum() {
		if(amountGivenCountOfBanknotes>=fixedCountGivenBanknotes){
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
					availableCurrentCountOfBanknote=fixedCountGivenBanknotes-amountGivenCountOfBanknotes;
					if(resultValueOfBanknote>currentCountOfBanknote){
						countHelper=currentCountOfBanknote;
						if(currentCountOfBanknote>availableCurrentCountOfBanknote){
							currentCountOfBanknote=availableCurrentCountOfBanknote;
						}
						saveCurrentAmount+=currentCountOfBanknote*currentValueOfBanknote;
						saveStorage.get(index2).setCount(currentCountOfBanknote);
						missingAmount=requiredAmount-saveCurrentAmount;
						amountGivenCountOfBanknotes+=currentCountOfBanknote;
						currentCountOfBanknote=countHelper-currentCountOfBanknote;
						banknoteStorageFromCurrentStorage.get(index2).setCount(currentCountOfBanknote);
					}else if(resultValueOfBanknote<=currentCountOfBanknote){
						if(resultValueOfBanknote>availableCurrentCountOfBanknote){
							resultValueOfBanknote=availableCurrentCountOfBanknote;
						}
						saveCurrentAmount+=resultValueOfBanknote*currentValueOfBanknote;
						saveStorage.get(index2).setCount(resultValueOfBanknote);
						amountGivenCountOfBanknotes+=resultValueOfBanknote;
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
			if(amountGivenCountOfBanknotes>=fixedCountGivenBanknotes){
				return saveCurrentAmount;
			}
		}
		System.out.println("saveCurrentSum="+saveCurrentAmount);
		return saveCurrentAmount;
	}
	
	public int putCash(BanknoteStorage banknoteStorageInput) throws IOException, CloneNotSupportedException {
		banknoteStorageFromCurrentStorage=getCopyStorageBanknote(banknoteStorageCurrent.getBanknotes());
		saveStorage=new BanknoteStorage().getBanknotes();
		List<Banknote> storage=banknoteStorageFromCurrentStorage;
		int saveCurrentAmountPutCash=0;
		int currentCountOfBanknotePutCash=0;
		int currentValueOfBanknotePutCash=0;
		int inputCountOfBanknotePutCash=0;
		int requiredAmountPutCash=0;
		
		requiredAmountPutCash=getSumFromBanknoteStorageInput(banknoteStorageInput.getBanknotes());
		for(int index=0;index<storage.size();index++){
			if(storage.get(index).getValue()==banknoteStorageInput.getBanknotes().get(index).getValue()){
				if (banknoteStorageInput.getBanknotes().get(index).getCount() != 0) {
					currentCountOfBanknotePutCash = storage.get(index).getCount();
					currentValueOfBanknotePutCash = storage.get(index).getValue();
					inputCountOfBanknotePutCash = banknoteStorageInput.getBanknotes().get(index).getCount();
					saveStorage.get(index).setValue(currentValueOfBanknotePutCash);
					if (currentCountOfBanknotePutCash < maxCountOfValueFromFile) {
						if (inputCountOfBanknotePutCash > currentCountOfBanknotePutCash) {
							if ((inputCountOfBanknotePutCash + currentCountOfBanknotePutCash) > maxCountOfValueFromFile) {
								inputCountOfBanknotePutCash = maxCountOfValueFromFile - currentCountOfBanknotePutCash;
							}
							saveCurrentAmountPutCash += inputCountOfBanknotePutCash * currentValueOfBanknotePutCash;
							saveStorage.get(index).setCount(inputCountOfBanknotePutCash);
							currentCountOfBanknotePutCash += inputCountOfBanknotePutCash;
							storage.get(index).setCount(currentCountOfBanknotePutCash);
						} else if (inputCountOfBanknotePutCash <= currentCountOfBanknotePutCash) {
							if ((inputCountOfBanknotePutCash + currentCountOfBanknotePutCash) > maxCountOfValueFromFile) {
								inputCountOfBanknotePutCash = maxCountOfValueFromFile - currentCountOfBanknotePutCash;
							}
							saveCurrentAmountPutCash += inputCountOfBanknotePutCash * currentValueOfBanknotePutCash;
							saveStorage.get(index).setCount(inputCountOfBanknotePutCash);
							currentCountOfBanknotePutCash += inputCountOfBanknotePutCash;
							storage.get(index).setCount(currentCountOfBanknotePutCash);
						}
					}
				}
			}
			if(requiredAmountPutCash==saveCurrentAmountPutCash){
				System.out.println("saveCurrentAmount="+saveCurrentAmountPutCash);
				return saveCurrentAmountPutCash;
			}
		}
		
		return saveCurrentAmountPutCash;
	}
	
	public int getSumFromBanknoteStorageInput(List<Banknote> storage) {
		int requiredAmount=0;
		for (Banknote banknote : storage) {
			if(banknote.getCount()!=0){
				requiredAmount+=banknote.getValue()*banknote.getCount();
			}
		}
		return requiredAmount;
	}	
	
	public List<Banknote> getBanknoteStorageFromFile(BanknoteStorage banknoteStorage) throws IOException {
		int resultValues[] = getArrayPropertiesBanknotes("value");
		int resultCounts[] = getArrayPropertiesBanknotes("count");
		int index = 0;
		for (Banknote banknote : banknoteStorage.getBanknotes()) {
			banknote.setValue(resultValues[index]);
			banknote.setCount(resultCounts[index]);
			index++;
		}

		return banknoteStorage.getBanknotes();
	}
	
	public int getValueOfNameFromFile(String inputValue) {
		int intResult;
		intResult=Integer.parseInt(properties.getProperty(inputValue));
		return intResult;
	}
	
	public List<Banknote> getSaveStorage() {
		return saveStorage;
	}
	
	private List<Banknote> getCopyStorageBanknote(List<Banknote> inputStorage) throws CloneNotSupportedException {
		List<Banknote>cloneStorage=new ArrayList<Banknote>(inputStorage.size());
		for(Banknote banknote: inputStorage){
			cloneStorage.add(banknote.clone());
		}
		return cloneStorage;
	}
	
	public void	saveCurrentStorageInMemory() throws CloneNotSupportedException{
		banknoteStorageCurrent.setBanknotes(getCopyStorageBanknote(banknoteStorageFromCurrentStorage));
		
	}
	
	private int[] getArrayPropertiesBanknotes(String value) throws IOException {
		int intResults[] = new int[0];
		try {
			String strValues[] = properties.getProperty(value).split(";");
			intResults = new int[strValues.length];
			for (int i = 0; i < intResults.length; i++) {
				intResults[i] = Integer.valueOf(strValues[i]);
			}
		} catch (Exception e) {
			System.out.println("File not found or damaged.");
			log.info("File not found or damaged. " + e.fillInStackTrace());
			e.printStackTrace();
		}
		return intResults;
	}

}
