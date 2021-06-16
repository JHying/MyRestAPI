package tw.hyin.mySpringBoot.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tw.hyin.mySpringBoot.pojo.ResponseObj;
import tw.hyin.mySpringBoot.pojo.ResponseObj.RspMsg;
import tw.hyin.mySpringBoot.pojo.UploadFileObj;
import tw.hyin.mySpringBoot.service.FileService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Path;

/**
 * @author H-yin on 2020.
 */
@RestController
@ResponseBody
@RequestMapping(value = "/file")
public class FileController extends BaseController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @ApiOperation(value = "檔案上傳")
    @PostMapping(value = "/upload")
    public ResponseEntity<ResponseObj<RspMsg>> fileUpload(@RequestParam("file") MultipartFile file,
                                                          @ModelAttribute UploadFileObj uploadFileObj) {
        try {
            if (fileService.upload(file, uploadFileObj)) {
                fileService.addAttachment(uploadFileObj);
                return super.sendSuccessRsp(RspMsg.SUCCESS);
            } else {
                return super.sendFailRsp(new Exception("file saved failed."));
            }
        } catch (Exception e) {
            return super.sendFailRsp(e);
        }
    }

    @ApiOperation(value = "檔案下載")
    @GetMapping(value = "/download/**")
    public ResponseEntity<?> fileDownloadWithWatermark(HttpServletRequest request) {
        try {
            String requestURL = request.getRequestURL().toString();
            String filePath = URLDecoder.decode(requestURL.split("file/download/")[1], "UTF-8");
            // 獲取檔案
            Path fullPath = fileService.getFullPath(filePath);
            File file = fullPath.toFile();
            if (!file.exists()) {
                return super.sendBadRequestRsp(RspMsg.NOT_FOUND);
            }
            //回傳檔案串流
            return super.downloadFile(file);
        } catch (Exception e) {
            return super.sendFailRsp(e);
        }
    }

}
