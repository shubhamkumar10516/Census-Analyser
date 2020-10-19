package censusanalyser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {
	private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
	private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
	private static final String INCORRECT_FILE_TYPE_GIVEN = "./src/main/resources/wrong_file_type.txt";

	@Test
	public void givenIndianCensusCSVFileReturnsCorrectRecords() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
			Assert.assertEquals(29, numOfRecords);
		} catch (CensusAnalyserException e) {
		}
	}

	@Test
	public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			boolean check = CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM.equals(e.type) || CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE.equals(e.type);
			Assert.assertTrue(check);
		}
	}

	@Test
	public void GivenTheStateCensusCsvFile_IfTypeIsWrong_ThrowCensusAnalyserException() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaCensusData(INCORRECT_FILE_TYPE_GIVEN);
		} catch (CensusAnalyserException e) {
			Assert.assertEquals(CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE, e.type);
		}
	}
}
