package com.company.courtmanagement.service;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.Judge;
import com.company.courtmanagement.repository.CourtRepository;
import com.company.courtmanagement.repository.JudgeRepository;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.impl.JudgeServiceImpl;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JudgeServiceTest {
    private JudgeRepository judgeRepository = mock(JudgeRepository.class);
    private CourtRepository courtRepository = mock(CourtRepository.class);
    private JudgeService judgeService = new JudgeServiceImpl(judgeRepository, courtRepository);

    @Test(expected = ResourceNotValidException.class)
    public void testJudgeSave_whenJudgeNull_thenFail() {
        judgeService.saveJudge(null);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testJudgeSave_whenCourtNotExists_thenFail() {
        Judge judge = buildJudge();
        when(courtRepository.existsById(1L)).thenReturn(false);

        judgeService.saveJudge(judge);
    }

    @Test
    public void testJudgeSave_whenValid_thenSaveSuccessfully() {
        Judge judge = buildJudge();
        when(courtRepository.existsById(1L)).thenReturn(true);

        judgeService.saveJudge(judge);
    }

    private Judge buildJudge() {
        Judge judge = new Judge();
        judge.setFirstName("firstName");
        judge.setSurname("surName");
        judge.setPatronymic("patronymic");
        judge.setCourt(new Court(1L));
        return judge;
    }
}
