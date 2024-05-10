package com.example.antiplagiarism.service.database;

import com.example.antiplagiarism.mapper.TextTestMapper;
import com.example.antiplagiarism.repository.db.TextTestRepository;
import com.example.antiplagiarism.service.IService;
import com.example.antiplagiarism.service.model.TextTestDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class TextTestService implements IService<TextTestDto, Long> {

    public static final List<String> TRIADS = List.of("раз", "про", "кур", "кот",
            "сиг", "одн", "воз", "рак", "сис", "вре");
    private static final String SENTENCE_SPLIT_PATTERN = "[.!?]\\s+";

    private final TextTestRepository textTestRepository;
    private final TextTestMapper textTestMapper;

    @Override
    public List<TextTestDto> findAll() {
        // not yet implemented
        throw new UnsupportedOperationException("Not implemented yet!");
    }


    @Override
    public TextTestDto save(TextTestDto entity) {
        return textTestMapper.toDto(textTestRepository.save(textTestMapper.toEntity(entity)));
    }

    @Override
    public void updateAll(List<TextTestDto> entities) {
        // not implemented yed
        throw new UnsupportedOperationException("Method not yet implemented!");
    }

    @Override
    public long count() {
        return textTestRepository.count();
    }

    public TextTestDto doTextTestAndSaveResult(TextTestDto textTestDto) {
        TextTestDto result = doTextTest(textTestDto);
        return save(result);
    }

    private TextTestDto doTextTest(TextTestDto textTestDto) {
        final String[] sentencesOne = doSentenceSplit(textTestDto.getTextOne());
        final String[] sentencesTwo = doSentenceSplit(textTestDto.getTextTwo());
        doEqualizeSentences(sentencesOne, sentencesTwo);
        final Integer[][] matrixFirst = buildTriadsMatrix(sentencesOne);
        final Integer[][] matrixSecond = buildTriadsMatrix(sentencesTwo);
        final double[][] correlationMatrixFirst = buildCorrelationMatrix(matrixFirst);
        final double[][] correlationMatrixSecond = buildCorrelationMatrix(matrixSecond);
        final BigDecimal equality = BigDecimal.valueOf(calculateEquality(correlationMatrixFirst, correlationMatrixSecond) * 100.0);
        textTestDto.setPlagiatResult(equality);
        return textTestDto;
    }

    public String[] doSentenceSplit(String text) {
        return text.toLowerCase(Locale.forLanguageTag("ru"))
                .trim().split(SENTENCE_SPLIT_PATTERN);
    }

    public void doEqualizeSentences(String[] sentencesOne, String[] sentencesTwo) {
        if (sentencesOne.length < sentencesTwo.length) {
            int originalLength = sentencesOne.length;
            sentencesOne = Arrays.copyOf(sentencesOne, sentencesTwo.length);
            Arrays.fill(sentencesOne, originalLength - 1, sentencesOne.length, "");
        } else if (sentencesOne.length > sentencesTwo.length) {
            int originalLength = sentencesTwo.length;
            sentencesTwo = Arrays.copyOf(sentencesTwo, sentencesOne.length);
            Arrays.fill(sentencesTwo, originalLength - 1, sentencesTwo.length, "");
        }
    }

    public Integer[][] buildTriadsMatrix(String[] sentences) {
        Integer[][] matrix = new Integer[TRIADS.size()][sentences.length];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = searchForEntries(TRIADS.get(i), sentences);
        }
        return matrix;
    }

    private Integer[] searchForEntries(String triada, String[] sentences) {
        Integer[] entries = new Integer[sentences.length];
        for (int i = 0; i < sentences.length; i++) {
            String escapedWord = Pattern.quote(triada);
            String pattern = ".*" + escapedWord + ".*";
            Pattern wordPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = wordPattern.matcher(sentences[i]);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            entries[i] = count;
        }
        return entries;
    }

    public double[][] buildCorrelationMatrix(Integer[][] matrix) {
        double[][] matrixDecimal = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrixDecimal[i][j] = matrix[i][j].doubleValue();
            }
        }
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrixDecimal);
        double[][] data = correlation.getCorrelationMatrix().getData();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (Double.isNaN(data[i][j])) {
                    data[i][j] = 0.0;
                }
            }
        }
        return data;
    }

    private double calculateEquality(double[][] matrixOne, double[][] matrixTwo) {
        // calculating Euclidean distance and applying maximum distance to result
        double sumOfSquares = 0.0;
        double maxDistance = maxEuclideanDistance(matrixOne.length);
        for (int j = 0; j < matrixOne[0].length; j++) {
            double columnDistance = 0.0;
            for (int i = 0; i < matrixOne.length; i++) {
                double difference = matrixOne[i][j] - matrixTwo[i][j];
                columnDistance += Math.pow(difference, 2);
            }
            sumOfSquares += (maxDistance - Math.sqrt(columnDistance)) / maxDistance;
        }

        return sumOfSquares / matrixOne[0].length;
    }

    private double maxEuclideanDistance(int length) {
        double result = 0.0;
        for (int i = 0; i < length; i++) {
            result += 1; // maximum distance is 1
        }
        return Math.sqrt(result);
    }

}
