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
// TODO: 8/11/16 eugene - if its Cloneable, where is clone() method?
public class Operations implements Cloneable{
	// TODO: 8/11/16 eugene - static final constants should be in uppercase
	private static final Logger log = Logger.getLogger(Operations.class);
	// TODO: 8/11/16 eugene - bad name for field. It's not a path to file
	private static InputStream filePath;
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
	private int fixedCountGivenBanknotes;
	// TODO: 8/11/16 eugene - bad name for field
	private List<Banknote> saveStorage;
	private int requiredAmount;
	// TODO: 8/11/16 eugene - bad name for field
	private int saveCurrentAmount;
	// TODO: 8/11/16 eugene - bad name for field
	private int amountGivenCountOfBanknotes;
	// TODO: 8/11/16 eugene - bad name for field
	private int countHelper;
	// TODO: 8/11/16 eugene - bad name for field
	private int availableCurrentCountOfBanknote;
	private int currentCountOfBanknote;
	private int currentValueOfBanknote;

	public Operations(InputStream url) {
		try {
			Operations.filePath = url;
			properties=new Properties();
			properties.load(filePath);
			// TODO: 8/11/16 eugene - do not leave commented code
//			filePath.close();
			banknoteStorageCurrent=new BanknoteStorage();
			// TODO: 8/11/16 eugene - this is really strange construction
			// TODO: 8/11/16 eugene - for getting banknote list you create another BanknoteStorage
			banknoteStorageCurrent.setBanknotes(getBanknoteStorageFromFile(new BanknoteStorage()));
			maxCountOfValueFromFile=getValueOfNameFromFile("maxCount");
			fixedCountGivenBanknotes=getValueOfNameFromFile("fixedCountGiven");
		} catch (IOException e) {
			// TODO: 8/11/16 eugene - is it really normal to continue working if your key instance broke down?
			log.info("File not found or damaged. " + e.fillInStackTrace());
		}

	}

	public static InputStream getFilePath() {
		return filePath;
	}

	// TODO: 8/11/16 eugene - overcomplicated method. Rework and split into separate methods
	// TODO: 8/11/16 eugene - 'get' means this is getter-method. Rename or make it getter
	// TODO: 8/11/16 eugene - not informative argument name
	public int getCash(BanknoteStorage banknoteStorageInput) throws IOException, CloneNotSupportedException {
		banknoteStorageFromCurrentStorage=getCopyStorageBanknote(banknoteStorageCurrent.getBanknotes());
		saveStorage=new BanknoteStorage().getBanknotes();
		// TODO: 8/11/16 eugene - this is really too much
		saveCurrentAmount=0;
		amountGivenCountOfBanknotes = 0;
		countHelper=0;
		requiredAmount=0;
		availableCurrentCountOfBanknote=0;
		currentCountOfBanknote=0;
		currentValueOfBanknote=0;
		// TODO: 8/11/16 eugene - no need in declaring this variable here. Move it inside loop
		int inputCountOfBanknote=0;

		requiredAmount=getSumFromBanknoteStorageInput(banknoteStorageInput.getBanknotes());
		for (int index=0;index < banknoteStorageFromCurrentStorage.size();index++) {
			// TODO: 8/11/16 eugene - banknoteStorageFromCurrentStorage.get(index) copypasted several times. Use variable
			// TODO: 8/11/16 eugene - if you refactor code, you can join both 'if' blocks into one: if (inputBanknote.getCount() != 0 && currentBanknote.getValue()== inputBanknote.getValue())
			if(banknoteStorageFromCurrentStorage.get(index).getValue()==banknoteStorageInput.getBanknotes().get(index).getValue()){
				if (banknoteStorageInput.getBanknotes().get(index).getCount() != 0) {
					currentCountOfBanknote = banknoteStorageFromCurrentStorage.get(index).getCount();
					currentValueOfBanknote = banknoteStorageFromCurrentStorage.get(index).getValue();
					// TODO: 8/11/16 eugene - banknoteStorageInput.getBanknotes().get(index) copypasted several times. Use variable
					inputCountOfBanknote = banknoteStorageInput.getBanknotes().get(index).getCount();
					saveStorage.get(index).setValue(currentValueOfBanknote);
					availableCurrentCountOfBanknote = fixedCountGivenBanknotes - amountGivenCountOfBanknotes;
					// TODO: 8/11/16 eugene - too much 'if' statements complicate code
					if (availableCurrentCountOfBanknote > 0) {
						// TODO: 8/11/16 eugene - in both conditions code is almost the same. Extract it to some method
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
			// TODO: 8/11/16 eugene - you could merge this conditions
			if(requiredAmount==saveCurrentAmount){
				return saveCurrentAmount;
			}
			if(amountGivenCountOfBanknotes>=fixedCountGivenBanknotes){
				return saveCurrentAmount;
			}
		}
		// TODO: 8/11/16 eugene - use logger
		System.out.println("saveCurrentSum="+saveCurrentAmount);
		return saveCurrentAmount;
	}

	// TODO: 8/11/16 eugene - this method looks like previous. Same method - same problems
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

	// TODO: 8/11/16 eugene - it all looks pretty much the same
	public int putCash(BanknoteStorage banknoteStorageInput) throws IOException, CloneNotSupportedException {
		banknoteStorageFromCurrentStorage=getCopyStorageBanknote(banknoteStorageCurrent.getBanknotes());
		saveStorage=new BanknoteStorage().getBanknotes();
		List<Banknote> storage=banknoteStorageFromCurrentStorage;
		int saveCurrentAmountPutCash=0;
		// TODO: 8/11/16 eugene - useless assignment to variables 
		// TODO: 8/11/16 eugene - it seems you don't need some of your class fields
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

						// TODO: 8/11/16 eugene - this code is exact copy of previous
						// TODO: 8/11/16 eugene - it seems this will be always true
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
				// TODO: 8/11/16 eugene - better use 'break;' here. Thus you have single 'return'
				return saveCurrentAmountPutCash;
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
	public List<Banknote> getBanknoteStorageFromFile(BanknoteStorage banknoteStorage) throws IOException {
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

	// TODO: 8/11/16 eugene - maybe 'cloneBanknotes' is a better name?
	private List<Banknote> getCopyStorageBanknote(List<Banknote> inputStorage) throws CloneNotSupportedException {
		// TODO: 8/11/16 eugene - starting from java 7 type in constructor call is redundant
		List<Banknote>cloneStorage=new ArrayList<Banknote>(inputStorage.size());
		for(Banknote banknote: inputStorage){
			cloneStorage.add(banknote.clone());
		}
		return cloneStorage;
	}

	public void	saveCurrentStorageInMemory() throws CloneNotSupportedException{
		banknoteStorageCurrent.setBanknotes(getCopyStorageBanknote(banknoteStorageFromCurrentStorage));
		// TODO: 8/11/16 eugene - redundant linebreaks

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
			// TODO: 8/11/16 eugene - better just log e.getStackTrace
			System.out.println("File not found or damaged.");
			log.info("File not found or damaged. " + e.fillInStackTrace());
			e.printStackTrace();
		}
		return intResults;
	}

}
