package ta4jexamples.loaders;

import au.com.bytecode.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yony on 3/29/17.
 */
public class CsvFileLoader {

    /**
     * @param CsvFilePath   String          - Path to the file that need to be parsed
     * @param headerSize    int             - Number of lines (from the beginning) that should be removed
     * @return csvData      List<String[]>  - List that contains array of strings. List or rows / Array of columns.
     */
    public static List<String[]> loadFileCSV(String CsvFilePath, int headerSize) {

        // load History and Current data on CSV
        InputStream         stream          = CsvTradesLoader.class.getClassLoader().getResourceAsStream(CsvFilePath);
        CSVReader           csvReader       = null;
        List<String[]>      csvData         = null;

        try {
            csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',');
            csvData = csvReader.readAll();
        }
        catch (IOException ioe) {
            Logger.getLogger(CsvTradesLoader.class.getName()).log(Level.SEVERE, "Unable to load trades from CSV", ioe);
        }
        finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                }
                catch (IOException ioe) {}
            }
        }

        // Remove CSV header
        if (headerSize > csvData.size()){
            throw new IndexOutOfBoundsException("CSV Header is bigger then given CSV data.");
        }
        for (int i=0 ; i<headerSize ; i++){
            csvData.remove(0);
        }

        return csvData;
    }

    public static List<String[]> loadFileCSV(String CsvFilePath) {
        return loadFileCSV(CsvFilePath, 0);
    }

    //================================================================================================

    public static void main(String args[]) throws IOException {
        List<String[]> csvData = loadFileCSV("nyseCompanies.csv", 0);

        // Print columns
        for (String[] line : csvData) {
            System.out.println(line[0]);
        }

    }

}