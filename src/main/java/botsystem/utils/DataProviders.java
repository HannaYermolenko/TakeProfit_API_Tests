package botsystem.utils;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class DataProviders {

    @DataProvider(name = "botData")
    public Iterator<Object[]> getDataFromCSV() throws IOException, CsvException {
        String filePath = "src/test/resources/money.csv";


        CSVReader csvReader = new CSVReader(new FileReader(filePath));
        List<String[]> allData = csvReader.readAll();

        return allData.stream()
                .map(data -> (Object[]) data)
                .iterator();
    }


    @DataProvider(name = "botData2")
    public Iterator<Object[]> getDataFromCSV2() throws IOException, CsvException {
        String filePath = "src/test/resources/ValidLongBotTest.csv";

        // Чтение CSV-файла
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> allData = csvReader.readAll();

            // Преобразуем строки CSV в массивы объектов для теста
            return allData.stream()
                    .map(data -> new Object[]{
                            data[0],                           // tradingPair (например, BTCUSDT)
                            data[1],                           // type (например, Long)
                            Integer.parseInt(data[2]),         // deposit (например, 10)
                            Boolean.parseBoolean(data[3]),     // stopLoss (например, false)
                            Boolean.parseBoolean(data[4]),     // takeProfit (например, true)
                            Boolean.parseBoolean(data[5]),     // pumpDump (например, false)
                            data[6],                           // indicator (например, RSI)
                            Integer.parseInt(data[7]),         // period (например, 1)
                            data[8]                            // interval (например, 1s)
                    })
                    .iterator();
        }
    }

    @DataProvider(name = "botData3")
    public Iterator<Object[]> getDataFromCSV3() throws IOException, CsvException {
        String filePath = "src/test/resources/ValidShortBotTest.csv";

        // Чтение CSV-файла
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> allData = csvReader.readAll();

            // Преобразуем строки CSV в массивы объектов для теста
            return allData.stream()
                    .map(data -> new Object[]{
                            data[0],                           // tradingPair (например, BTCUSDT)
                            data[1],                           // type (например, Long)
                            Integer.parseInt(data[2]),         // deposit (например, 10)
                            Boolean.parseBoolean(data[3]),     // stopLoss (например, false)
                            Boolean.parseBoolean(data[4]),     // takeProfit (например, true)
                            Boolean.parseBoolean(data[5]),     // pumpDump (например, false)
                            data[6],                           // indicator (например, RSI)
                            Integer.parseInt(data[7]),         // period (например, 1)
                            data[8]                            // interval (например, 1s)
                    })
                    .iterator();
        }
    }

    @DataProvider(name = "tradingPairs")
    public Iterator<Object[]> getTradingPairsFromCSV() throws IOException, CsvException {
        String filePath = "src/test/resources/InvalidBotsData.csv";

        // Чтение CSV-файла
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> allData = csvReader.readAll();

            // Преобразуем строки CSV в массивы объектов для теста
            return allData.stream()
                    .map(data -> new Object[]{data[0]}) // data[0] содержит единственное значение - торговую пару
                    .iterator();
        }
    }


    @DataProvider(name = "tradingPairsBots")
    public Iterator<Object[]> tradingPairsForeBotsCSV() throws IOException, CsvException {
        String filePath = "src/test/resources/paarMoneyForBot.csv";

        // Чтение CSV-файла
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> allData = csvReader.readAll();

            // Преобразуем строки CSV в массивы объектов для теста
            return allData.stream()
                    .map(data -> new Object[]{data[0]}) // data[0] содержит единственное значение - торговую пару
                    .iterator();
        }
    }

}
