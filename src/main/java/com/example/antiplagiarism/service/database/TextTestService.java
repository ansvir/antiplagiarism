package com.example.antiplagiarism.service.database;

import com.example.antiplagiarism.mapper.TextTestMapper;
import com.example.antiplagiarism.repository.db.TextTestRepository;
import com.example.antiplagiarism.service.IService;
import com.example.antiplagiarism.service.model.ProfileDto;
import com.example.antiplagiarism.service.model.TextTestDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TextTestService implements IService<TextTestDto, Long> {

    public static final List<String> TRIADS = List.of("раз", "про", "кур", "кот",
            "сиг", "одн", "воз", "рак", "сис", "вре");
    private static final String SENTENCE_SPLIT_PATTERN = "[.!?]\\s+";

    private final TextTestRepository textTestRepository;
    private final TextTestMapper textTestMapper;
    private final ProfileService profileService;

    @Override
    public List<TextTestDto> findAll() {
        throw new NotYetImplementedException("Not yet implemented!");
    }

    public TextTestDto findById(Long id) {
        return textTestRepository.findById(id)
                .map(textTestMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("No such text test for id: " + id));
    }

    @Override
    public TextTestDto save(TextTestDto entity) {
        return textTestMapper.toDto(textTestRepository.save(textTestMapper.toEntity(entity)));
    }

    @Override
    public void updateAll(List<TextTestDto> entities) {
        throw new NotYetImplementedException("Not yet implemented!");
    }

    @Override
    public long count() {
        return textTestRepository.count();
    }

    public TextTestDto doTextTestAndSaveResult(TextTestDto textTestDto, String username) {
        TextTestDto result = doTextTest(textTestDto);
        ProfileDto profileDto = profileService.findByUsername(username);
        profileDto.getTextTestDtos().add(result);
        ProfileDto saved = profileService.save(profileDto);
        List<TextTestDto> foundSorted = saved.getTextTestDtos()
                .stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        return foundSorted.get(foundSorted.size() - 1);
    }

    private TextTestDto doTextTest(TextTestDto textTestDto) {
        String[] sentencesOne = doSentenceSplit(textTestDto.getTextOne());
        String[] sentencesTwo = doSentenceSplit(textTestDto.getTextTwo());
        if (sentencesOne.length > sentencesTwo.length) {
            sentencesTwo = doEqualizeSentences(sentencesTwo, sentencesOne);
        } else if (sentencesOne.length < sentencesTwo.length) {
            sentencesOne = doEqualizeSentences(sentencesOne, sentencesTwo);
        }
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

    public String[] doEqualizeSentences(String[] sentencesOne, String[] sentencesTwo) {
        int originalLength = sentencesOne.length;
        sentencesOne = Arrays.copyOf(sentencesOne, sentencesTwo.length);
        Arrays.fill(sentencesOne, originalLength, sentencesOne.length, "");
        return sentencesOne;
    }

    public Integer[][] buildTriadsMatrix(String[] sentences) {
        Integer[][] matrix = new Integer[sentences.length][TRIADS.size()];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = searchForEntries(TRIADS.toArray(String[]::new), sentences[i]);
        }
        return matrix;
    }

    private Integer[] searchForEntries(String[] triads, String sentence) {
        Integer[] entries = new Integer[triads.length];
        for (int i = 0; i < triads.length; i++) {
            String escapedWord = Pattern.quote(triads[i]);
            String pattern = ".*" + escapedWord + ".*";
            Pattern wordPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = wordPattern.matcher(sentence);
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
