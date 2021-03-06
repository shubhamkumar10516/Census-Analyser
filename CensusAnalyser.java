package censusanalyser;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import censusanalyser.CensusAnalyserException.ExceptionType;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
	Iterator<IndiaCensusCSV> censusCSVIterator = null;

	public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException, IlleagalStateException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			if(!checkHeader(reader)) 
				   throw new CensusAnalyserException("Header is not matching", ExceptionType.INCORRECT_HEADER_TYPE);;
				
			CsvToBeanBuilder<IndiaCensusCSV> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
			csvToBeanBuilder.withType(IndiaCensusCSV.class);
			csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
			CsvToBean<IndiaCensusCSV> csvToBean = csvToBeanBuilder.build();
			censusCSVIterator = csvToBean.iterator();
			Iterable<IndiaCensusCSV> csvIterable = () -> censusCSVIterator;
			int namOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
			return namOfEateries;
		}catch (NoSuchFileException e) {
            throw new CensusAnalyserException("Please enter correct file", CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE);
        }catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		} catch (IllegalStateException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
		} catch (RuntimeException e){
            throw new CensusAnalyserException("there can be delimiter issue in file",CensusAnalyserException.ExceptionType.DELIMITER_ISSUE);
        }
	}
    // checking for header correctness
	private boolean checkHeader(Reader reader) throws IOException {
		CSVReader csvReader = new CSVReader(reader);
		String[] nextRecord;
		nextRecord = csvReader.readNext();
		boolean result = nextRecord[0].equals("State") && nextRecord[1].equals("Population") && nextRecord[2].equals("AreaInSqKm")
				&& nextRecord[3].equals("DensityPerSqKm");
		System.out.println("---------------"+result);
		return result;
	}

	public int loadIndiaStateCode(String csvFilePath)throws CensusAnalyserException, IlleagalStateException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			CsvToBeanBuilder<IndiaStateCodeCSV> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
			csvToBeanBuilder.withType(IndiaStateCodeCSV.class);
			csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
			CsvToBean<IndiaStateCodeCSV> csvToBean = csvToBeanBuilder.build();
			Iterator<IndiaStateCodeCSV> censusCSVIterator = csvToBean.iterator();
			Iterable<IndiaStateCodeCSV> csvIterable = () -> censusCSVIterator;
			int namOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
			return namOfEateries;
		}catch (NoSuchFileException e) {
            throw new CensusAnalyserException("Please enter correct file", CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE);
        }catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		} catch (IllegalStateException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
		} catch (Exception e){
            throw new CensusAnalyserException("there can be delimiter issue in file",CensusAnalyserException.ExceptionType.DELIMITER_ISSUE);
        }
	}
}
