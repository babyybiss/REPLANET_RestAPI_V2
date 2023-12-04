package metaint.replanet.rest.bookmark.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import metaint.replanet.rest.auth.jwt.TokenProvider;
import metaint.replanet.rest.bookmark.dto.BookmarkDTO;
import metaint.replanet.rest.bookmark.dto.BookmarkRegistDTO;
import metaint.replanet.rest.bookmark.entity.Bookmark;
import metaint.replanet.rest.bookmark.service.BookmarkService;
import metaint.replanet.rest.campaign.dto.CampaignDesOrgDTO;
import metaint.replanet.rest.common.ResponseMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = {"/"})
public class BookmarkController {
    private BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService){
        this.bookmarkService = bookmarkService;
    }

    //북마크 전체조회
    @ApiOperation(value = "관심리스트 전체 조회", notes = "관심리스트 조회합니다", tags = {"관심리스트 조회"})
    @GetMapping("bookmarks")
    public ResponseEntity<ResponseMessageDTO> getBookmarkListByMember(@RequestParam String memberCode){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        List<BookmarkDTO> bookmarkList = bookmarkService.getBookmarkListByMember(memberCode);
        responseMap.put("bookmarkList", bookmarkList);

        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    // 북마크 등록
    @ApiOperation(value = "관심리스트 등록", notes = "관심리스트 등록합니다", tags = {"관심리스트 등록"})
    @PostMapping("bookmarks")
    public boolean addBookmark(@RequestBody BookmarkRegistDTO bookmarkDTO){
        boolean check = bookmarkService.addBookmark(bookmarkDTO);
        return check;
    }

    // 북마크 삭제
    @ApiOperation(value = "관심리스트 삭제", notes = "관심리스트 삭제합니다", tags = {"관심리스트 삭제"})
    @DeleteMapping("bookmarks")
    public boolean deleteBookmark(@RequestParam String memberCode, @RequestParam String campaignCode){
        boolean check = bookmarkService.deleteBookmark(memberCode,campaignCode);
        return check;
    }

    // 북마크 전체 삭제
    @PutMapping("AllBookmarks")
    public ResponseEntity<String> deleteBookmarks(@RequestBody List<Integer> bookmarkCode){
        bookmarkService.bookmarkDeleteAll(bookmarkCode);
        return null;
    }
}
