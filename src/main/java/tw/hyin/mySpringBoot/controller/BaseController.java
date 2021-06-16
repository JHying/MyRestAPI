package tw.hyin.mySpringBoot.controller;

import com.google.common.io.Files;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import tw.hyin.mySpringBoot.pojo.ResponseObj;
import tw.hyin.mySpringBoot.pojo.ResponseObj.RspMsg;
import tw.hyin.mySpringBoot.utils.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class BaseController {

    protected <T> ResponseEntity<ResponseObj<T>> sendSuccessRsp(T result) {
        return new ResponseEntity(ResponseObj.builder().status(HttpStatus.OK)
                .result(result).build(), HttpStatus.OK);
    }

    protected <T> ResponseEntity<ResponseObj<T>> sendFailRsp(Exception e) {
        e.printStackTrace();
        Log.error(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(RspMsg.FAILED.getMsg());
        return new ResponseEntity(ResponseObj.builder().status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errors(errors).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected <T> ResponseEntity<ResponseObj<T>> sendBadRequestRsp(T result) {
        List<String> errors = new ArrayList<>();
        errors.add(result.toString());
        return new ResponseEntity(ResponseObj.builder().status(HttpStatus.BAD_REQUEST)
                .errors(errors).build(), HttpStatus.BAD_REQUEST);
    }

    protected <T> ResponseEntity<?> downloadFile(File originFile) throws Exception {

        String mimeType = URLConnection.guessContentTypeFromName(originFile.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(Files.toByteArray(originFile));
        byteOut.close();
        //包在 resource 裡才能 return 給前台，否則會 406
        ByteArrayResource resource = new ByteArrayResource(byteOut.toByteArray());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=" + URLEncoder.encode(originFile.getName(), "UTF-8"))
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }

}
