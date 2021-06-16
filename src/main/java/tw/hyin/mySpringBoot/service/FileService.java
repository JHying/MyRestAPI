package tw.hyin.mySpringBoot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tw.hyin.mySpringBoot.dao.UploadRecordDao;
import tw.hyin.mySpringBoot.entity.UploadRecord;
import tw.hyin.mySpringBoot.pojo.UploadFileObj;
import tw.hyin.mySpringBoot.utils.FileUtil;
import tw.hyin.mySpringBoot.utils.Log;
import tw.hyin.mySpringBoot.utils.PojoUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static tw.hyin.mySpringBoot.config.web.UserInterceptor.USER;

/**
 * @author H-yin on 2020.
 */
@Service
@Transactional
public class FileService {

    @Value("${server.upload.rootpath}")
    private String rootPath;
    private final UploadRecordDao uploadRecordDao;

    public FileService(UploadRecordDao uploadRecordDao) {
        this.uploadRecordDao = uploadRecordDao;
    }

    /**
     * 上傳檔案
     */
    public boolean upload(MultipartFile uploadfile, UploadFileObj uploadFileObj) throws Exception {
        FileUtil fileUtil;
        Path fullPath = Paths.get(this.rootPath, uploadFileObj.getFilePath());

        if (uploadFileObj.getFileName() != null) {
            fileUtil = new FileUtil(fullPath, uploadFileObj.getFileName());
        } else {
            fileUtil = new FileUtil(fullPath);
        }

        return fileUtil.saveFile(uploadfile);
    }

    public Path getFullPath(String... filePath) throws Exception {
        return Paths.get(this.rootPath, filePath);
    }

    /**
     * 新增資料於資料庫
     */
    public void addAttachment(UploadFileObj uploadFileObj) throws Exception {
        //新增 UploadRecord
        FileUtil fileUtil = new FileUtil(this.rootPath + "/" + uploadFileObj.getFilePath());
        if (fileUtil.getFile().exists()) {
            UploadRecord uploadRecord = PojoUtil.convertPojo(uploadFileObj, UploadRecord.class);
            uploadRecord.setFilePath(uploadFileObj.getFilePath() + "/" + uploadFileObj.getFileName());
            uploadRecord.setUserID(USER.getUserId());
            uploadRecord.setUploadDate(new Date());
            uploadRecordDao.saveBean(uploadRecord);
            Log.info("insert into uploadRecord: " + this.rootPath + "/" + uploadRecord.getFilePath());
        } else {
            throw new Exception("File not exist! Fail to insert upload data.");
        }
    }

}
