package com.example.antiplagiarism.service.database;

import com.example.antiplagiarism.mapper.ProfileMapper;
import com.example.antiplagiarism.mapper.TextTestMapper;
import com.example.antiplagiarism.repository.db.ProfileRepository;
import com.example.antiplagiarism.repository.db.TextTestRepository;
import com.example.antiplagiarism.repository.db.UserRepository;
import com.example.antiplagiarism.repository.entity.Profile;
import com.example.antiplagiarism.repository.entity.TextTest;
import com.example.antiplagiarism.service.IService;
import com.example.antiplagiarism.service.model.ProfileDto;
import com.example.antiplagiarism.service.model.TextTestDto;
import lombok.Data;
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
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
@Transactional
public class TextTestService implements IService<TextTestDto, Long> {

    private static final List<String> TRIADS = List.of("раз", "про", "кур", "кот",
            "сиг", "одн", "воз", "рак", "сис", "вре");
    private static final String SENTENCE_SPLIT_PATTERN = "\\s*([^.!?]+(?:[.!?](?!['\"]?\\s|$)[^.!?]+)*)\\s*[.!?]?";

    private final TextTestRepository textTestRepository;
    private final ProfileRepository profileRepository;
    private final TextTestMapper textTestMapper;

    @Override
    public List<TextTestDto> findAll() {
        return textTestRepository.findAll()
                .stream()
                .map(textTestMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public TextTestDto save(TextTestDto entity) {
        return textTestMapper.toDto(textTestRepository.save(textTestMapper.toEntity(entity)));
    }

    @Override
    public void updateAll(List<TextTestDto> entities) {
        List<TextTest> textTests = entities
                .stream()
                .map(textTest -> {
                    ProfileDto profil
                    textTestMapper.toEntity(textTest, );
                })
                .collect(Collectors.toList());
        textTestRepository.saveAll(textTests);
    }

    @Override
    public long count() {
        return textTestRepository.count();
    }

    public TextTestDto testTextAndSaveResult(TextTestDto textTestDto) {
        final TextTestDto toSave = doTextTest(textTestDto);
        return save(toSave);
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

    private String[] doSentenceSplit(String text) {
        return text.toLowerCase(Locale.forLanguageTag("ru"))
                .trim().split(SENTENCE_SPLIT_PATTERN);
    }

    private void doEqualizeSentences(String[] sentencesOne, String[] sentencesTwo) {
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

    private Integer[][] buildTriadsMatrix(String[] sentencesOne) {
        Integer[][] matrix = new Integer[TRIADS.size()][sentencesOne.length];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = searchForEntries(TRIADS.get(i), sentencesOne);
        }
        return matrix;
    }

    private Integer[] searchForEntries(String triada, String[] sentences) {
        Integer[] entries = new Integer[sentences.length];
        for (int i = 0; i < sentences.length; i++) {
            String escapedWord = Pattern.quote(triada);
            String pattern = "\\b" + escapedWord + "\\b";
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

    private double[][] buildCorrelationMatrix(Integer[][] matrix) {
        double[][] matrixDecimal = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrixDecimal[i][j] = matrix[i][j].doubleValue();
            }
        }
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrixDecimal);
        return correlation.getCorrelationMatrix().getData();
    }

    private double calculateEquality(double[][] matrixOne, double[][] matrixTwo) {
        // calculating Euclidean distance and applying maximum distance to ech
        double sumOfSquares = 0.0;
        double maxDistance = maxEuclideanDistance(matrixOne.length);
        for (int j = 0; j < matrixOne[0].length; j++) {
            double columnDistance = 0.0;
            for (int i = 0; i < matrixOne.length; i++) {
                double difference = matrixOne[i][j] - matrixTwo[i][j];
                columnDistance += Math.pow(difference, 2);
            }
            sumOfSquares += columnDistance;
        }

        return (maxDistance - Math.sqrt(sumOfSquares)) / maxDistance;
    }

    private double maxEuclideanDistance(int length) {
        double result = 0.0;
        for (int i = 0; i < length; i++) {
            result += 1; // maximum distance is 1
        }
        return Math.sqrt(result);
    }

}
