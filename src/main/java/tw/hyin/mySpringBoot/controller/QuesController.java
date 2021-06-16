package tw.hyin.mySpringBoot.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.hyin.mySpringBoot.entity.QuestionnaireQuestion;
import tw.hyin.mySpringBoot.pojo.ResponseObj;
import tw.hyin.mySpringBoot.pojo.questionnaire.AddAnswerReq;
import tw.hyin.mySpringBoot.pojo.questionnaire.AnswerRsp;
import tw.hyin.mySpringBoot.pojo.questionnaire.QuestionnaireList;
import tw.hyin.mySpringBoot.service.QuestionnaireService;

import java.util.List;

import static tw.hyin.mySpringBoot.config.web.UserInterceptor.USER;
import static tw.hyin.mySpringBoot.pojo.ResponseObj.*;

/**
 * @author rita6 on 2021.
 */
@RestController
@ResponseBody
@RequestMapping(value = "/ques")
public class QuesController extends BaseController {

    private final QuestionnaireService qService;

    @Autowired
    public QuesController(QuestionnaireService qService) {
        this.qService = qService;
    }

    @ApiOperation(value = "取得問卷內容")
    @GetMapping(value = "/content/{questionnaireType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj<List<QuestionnaireQuestion>>> getQuestions(@PathVariable(value = "questionnaireType") String questionnaireType) {
        try {
            return super.sendSuccessRsp(qService.getQuestions(questionnaireType));
        } catch (Exception e) {
            return super.sendFailRsp(e);
        }
    }

    @ApiOperation(value = "取得使用者填答之問卷")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj<QuestionnaireList>> getQuestionnaires() {
        try {
            return super.sendSuccessRsp(qService.getQuestionnaire(USER));
        } catch (Exception e) {
            return super.sendFailRsp(e);
        }
    }

    @ApiOperation(value = "取得問卷填答內容")
    @GetMapping(value = "/answer/{qId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj<List<AnswerRsp>>> getAnswers(@PathVariable(value = "questionnaireId") Integer qId) {
        try {
            return super.sendSuccessRsp(qService.getAnswers(qId));
        } catch (Exception e) {
            return super.sendFailRsp(e);
        }
    }

    @ApiOperation(value = "新增問卷")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj<RspMsg>> addQuestionnaire(@RequestBody AddAnswerReq addAnswerReq) {
        try {
            qService.addAnswer(addAnswerReq, USER);
            return super.sendSuccessRsp(RspMsg.SUCCESS);
        } catch (Exception e) {
            return super.sendFailRsp(e);
        }
    }

    @ApiOperation(value = "編輯問卷")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj<RspMsg>> updateQuestionnaire(@RequestBody AddAnswerReq addAnswerReq) {
        try {
            qService.updateAnswer(addAnswerReq);
            return super.sendSuccessRsp(RspMsg.SUCCESS);
        } catch (Exception e) {
            return super.sendFailRsp(e);
        }
    }

    @ApiOperation(value = "刪除問卷")
    @DeleteMapping(value = "/delete/{questionnaireId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj<RspMsg>> deleteQuestionnaire(@PathVariable(value = "questionnaireId") Integer id) {
        try {
            qService.deleteQuestionaire(id, USER);
            return super.sendSuccessRsp(RspMsg.SUCCESS);
        } catch (Exception e) {
            return super.sendFailRsp(e);
        }
    }

}
