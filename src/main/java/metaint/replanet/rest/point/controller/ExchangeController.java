package metaint.replanet.rest.point.controller;

import metaint.replanet.rest.point.dto.ExchangeDTO;
import metaint.replanet.rest.point.dto.PointFileDTO;
import metaint.replanet.rest.point.service.ExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/exchanges")
public class ExchangeController {

    private ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService){
        this.exchangeService = exchangeService;
    }

    @Transactional
    @PostMapping("")
    public ResponseEntity<?> insertExchange (@RequestPart(value="title") String title,
                                            @RequestPart(value = "memberCode") String memberCodeStr,
                                            @RequestPart(value="file")MultipartFile pointFile){

        int memberCode = Integer.parseInt(memberCodeStr);

        log.info("제목 넘어왔나요?" + title);
        log.info("코드 넘어왔나요?" + memberCode);

        if(title != null && pointFile != null) {
            ExchangeDTO newExchange = new ExchangeDTO();
            newExchange.setExchangeDate(new Date());
            newExchange.setTitle(title);
            newExchange.setMemberCode(memberCode);

            log.info("DTO 확인 " + newExchange);

            try {
                int savedExchangeCode = exchangeService.insertExchange(newExchange);

                log.info("저장된 코드 확인 " + savedExchangeCode);

                String fileOriginName = pointFile.getOriginalFilename();
                String fileExtension = fileOriginName.substring(fileOriginName.lastIndexOf("."));
                String fileSaveName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;
                String FILE_DIR = null;
                Path filePath = Paths.get("/REPLANET_ReactAPI/public/exchangeFiles").toAbsolutePath();
                Path rootPath;
                if (FileSystems.getDefault().getSeparator().equals("/")) {
                    // Unix-like system (MacOS, Linux)
                    rootPath = Paths.get("/User").toAbsolutePath();
                    Path relativePath = rootPath.relativize(filePath);
                    FILE_DIR = String.valueOf(relativePath);
                } else {
                    // Windows
                    rootPath = Paths.get("C:\\").toAbsolutePath();
                    Path relativePath = rootPath.resolve(filePath);
                    FILE_DIR = String.valueOf(relativePath);
                }
                log.info("경로 확인 : " + FILE_DIR);

                PointFileDTO newFile = new PointFileDTO();
                newFile.setFileOriginName(fileOriginName);
                newFile.setFileSaveName(fileSaveName);
                newFile.setFilePath(FILE_DIR);
                newFile.setFileExtension(fileExtension);
                newFile.setApplicationCode(savedExchangeCode);

                try{
                    File directory = new File(FILE_DIR);
                    if(!directory.exists()){
                        directory.mkdirs();
                        log.info("저장경로가 존재하지 않아 새로 생성되었습니다.");
                    }
                    File pf = new File(filePath + "/" + fileSaveName);
                    pointFile.transferTo(pf);
                    exchangeService.insertPointFile(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ResponseEntity.status(HttpStatus.OK).body("신청 성공");
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("신청 오류");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신청 오류");
    }

    @GetMapping("")
    public List<ExchangeDTO> selectAllExchanges(){

        List<ExchangeDTO> exchangeList = exchangeService.selectAllExchanges();

        return exchangeList;
//        return ResponseEntity.status(HttpStatus.OK).body(exchangeList);
    }

//    @GetMapping("/{status}")
//    public List<ExchangeDTO> selectMemberAllExchange(@RequestParam(name = "status") String status){
//
//        List<ExchangeDTO> listByStatus = exchangeService.selectExchangesByStatus(status);
//
//        return listByStatus;
//    }

    @GetMapping("/{exchangeCode}")
    public ResponseEntity<Map<String, Object>> selectExchangeDetail(@PathVariable int exchangeCode){

        log.info("요청 받았니? " + exchangeCode);
        Map<String, Object> exchangeDetail = exchangeService.selectExchangeDetail(exchangeCode);
        log.info("백에서 넘겨주는 값 확인 : " + exchangeDetail);

        return ResponseEntity.status(HttpStatus.OK).body(exchangeDetail);
    }
}
