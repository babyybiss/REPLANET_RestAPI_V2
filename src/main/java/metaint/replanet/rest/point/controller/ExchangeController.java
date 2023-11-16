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
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/")
public class ExchangeController {

    private ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService){
        this.exchangeService = exchangeService;
    }

    @Transactional
    @PostMapping("exchanges")
    public ResponseEntity<?> insertExchange (@RequestPart(value="title") String title,
                                             @RequestPart(value="file") MultipartFile pointFile,
                                             @RequestPart(value = "memberCode") String memberCode){

        log.info("제목 넘어왔나요?" + title);
        log.info("코드 넘어왔나요?" + memberCode);

        if(title != null && pointFile != null) {
            ExchangeDTO newExchange = new ExchangeDTO();
            newExchange.setExchangeDate(new Date());
            newExchange.setTitle(title);
            newExchange.setMemberCode(Integer.parseInt(memberCode));

            log.info("DTO 확인 " + newExchange);

            try {
                int savedExchangeCode = exchangeService.insertExchange(newExchange);

                log.info("저장된 코드 확인 " + savedExchangeCode);

                String fileOriginName = pointFile.getOriginalFilename();
                String fileExtension = fileOriginName.substring(fileOriginName.lastIndexOf("."));
                String fileSaveName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;
                String FILE_DIR = null;
                Path rootPath;
                if (FileSystems.getDefault().getSeparator().equals("/")) {
                    // Unix-like system (MacOS, Linux)
                    Path filePath1 = Paths.get("/REPLANET_ReactAPI/public/exchangeFiles").toAbsolutePath();
                    rootPath = Paths.get("/User").toAbsolutePath();
                    Path relativePath = rootPath.relativize(filePath1);
                    FILE_DIR = String.valueOf(relativePath);
                } else {
                    // Windows
                    Path filePath2 = Paths.get("/dev/metaint/REPLANET_ReactAPI/public/exchangeFiles").toAbsolutePath();
                    rootPath = Paths.get("C:\\").toAbsolutePath();
                    Path relativePath = rootPath.resolve(filePath2);
                    FILE_DIR = String.valueOf(relativePath);
                }
                log.info("저장 경로 확인 : " + FILE_DIR);

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
                    File pf = new File(FILE_DIR + "/" + fileSaveName);
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

    @GetMapping("exchanges")
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

    @GetMapping("exchanges/{exchangeCode}")
    public ResponseEntity<Map<String, Object>> selectExchangeDetail(@PathVariable int exchangeCode){

        log.info("전환 신청 코드 " + exchangeCode);
        Map<String, Object> exchangeDetailA = exchangeService.selectExchangeDetailA(exchangeCode);
        log.info("조회 값 확인 : " + exchangeDetailA);
        return ResponseEntity.status(HttpStatus.OK).body(exchangeDetailA);
    }

    @GetMapping("users/{memberCode}/exchanges")
    public ResponseEntity<List<ExchangeDTO>> selectMemberAllExchange(@PathVariable int memberCode){

        log.info("memberCode 확인 : " + memberCode);
        List<ExchangeDTO> memberAllExchange = exchangeService.selectMemberAllExchange(memberCode);
        return ResponseEntity.status(HttpStatus.OK).body(memberAllExchange);
    }

    @GetMapping("users/exchangeDetail/{exchangeCode}")
    public ResponseEntity<?> selectExchangeDetailU(@PathVariable int exchangeCode){

        log.info("전환 신청 코드 : " + exchangeCode);
        Map<String, Object> exchangeDetailU = exchangeService.selectExchangeDetailU(exchangeCode);
        log.info("조회 값 확인 : " + exchangeDetailU);
        return ResponseEntity.status(HttpStatus.OK).body(exchangeDetailU);
    }

    @PutMapping("exchanges/{exchangeCode}")
    public ResponseEntity<?> updateExchangeStatus(@PathVariable int exchangeCode,
                                                  @RequestBody ExchangeDTO exchangeDTO){

        int result = 0;
        log.info("excjangeDTO 확인 : " + exchangeDTO);
        if(exchangeDTO.getStatus() == "승인"){
            result = exchangeService.exchangeApproval(exchangeDTO);
        } else if(exchangeDTO.getStatus() == "반려"){
            result = exchangeService.exchangeRejection(exchangeDTO);
        }
        if(result == 1){
            return ResponseEntity.status(HttpStatus.OK).body("신청 처리 완료");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신청 처리 중 오류 발생");
        }
    }
}
