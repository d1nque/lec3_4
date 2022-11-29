package org.example.model;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

public class SecondTask {
    private Map<String, Double> fineStat = new HashMap<>();

    /**
     * Метод для сортировки нашего Map и подальшей записи в файл xml
     * @param pathToFolderWithJson путь к папке с файлами json
     * @throws IOException
     */
    public void sortAndWriteFineStat(String pathToFolderWithJson) throws IOException {
        getSummaryOfFinesFromFiles(pathToFolderWithJson);
        List<Map.Entry<String, Double>> nlist = new ArrayList<>(fineStat.entrySet());
        nlist.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        Fines fines = new Fines();
        fines.setFines(getListFinesFromMapEntry(nlist));
        jacksonAnnotation2Xml(fines);
    }

    /**
     * Мне показалось самым простым способом для заполнения
     * структурированого и читаемого xml это создать отдельный описаный класс
     * @param nlist наш список на основе Map
     * @return наши штрафы в виде списка
     */
    private List<Fine> getListFinesFromMapEntry(List<Map.Entry<String, Double>> nlist){
        List<Fine> fineList = new LinkedList<>();
        for(int i = 0;i < nlist.size();i++){
            fineList.add(new Fine(nlist.get(i).getKey(), nlist.get(i).getValue()));
        }
        return fineList;
    }

    /**
     * Метод где мы перед путь к папке с файлами json
     * Пробегаемся по нашим файлам и собираем только тип и сумму штрафа
     * Использую Map, ключ уникальный, удобно, просто плюсуем штрафы
     * @param pathToFolderWithJson
     * @throws IOException
     */
    private void getSummaryOfFinesFromFiles(String pathToFolderWithJson) throws IOException {
        File folderWithJson = new File(pathToFolderWithJson);

        for(File file: Objects.requireNonNull(folderWithJson.listFiles())) {
            for (Fine fine : jacksonFileParserForFine(file)) {
                if (getFileExtension(file).equals("json")) {
                    if (!fineStat.containsKey(fine.type)) {
                        fineStat.put(fine.type, fine.fine_amount);
                    } else {
                        fineStat.put(fine.type, fineStat.get(fine.type) + fine.fine_amount);
                    }
                }
            }
        }
    }

    private void jacksonAnnotation2Xml(Fines fines) {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            xmlMapper.writeValue(new File("src/main/resources/result/fineResult.xml"), fines);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private List<Fine> jacksonFileParserForFine(File file) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        jsonMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        return jsonMapper.readValue(file, new TypeReference<>(){});

    }

    /**
     * Метод для случая, если в папке будут файлы не только типа json
     * @param file
     * @return расширение файла
     */
    private static String getFileExtension(File file) {
        String fileName = file.getName();

        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
            return fileName.substring(fileName.lastIndexOf(".")+1);
        }
        else {
            return "";
        }
    }

}


