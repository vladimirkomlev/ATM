package ua.dataart.school.atm.operations.behavior;

import java.io.IOException;

import ua.dataart.school.atm.storage.BanknoteStorage;

public interface OperationOfBanknote {
	
	int giveRequiredCash(BanknoteStorage inputValuesOfRequirentAmount) throws IOException, CloneNotSupportedException;
	int acceptInputCash(BanknoteStorage inputValuesOfAcceptAmount) throws IOException, CloneNotSupportedException;
	int giveRemainingAmountOfCash();

}
