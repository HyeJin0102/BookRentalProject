package com.office.library.book.admin.util;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	//파일 업로드
	public String upload(MultipartFile file) {
		boolean result = false;
			
		//파일 저장
		String fileOriName = file.getOriginalFilename(); //관리자가 업로드한 원본 파일 이름 가져오기
		String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."), fileOriName.length()); //원본 파일 확장자 가져오기
		String uploadDir = "C:\\HJ_Project\\BookRentalProject\\book_upload\\"; //서버에서 파일이 저장되는 위치 정의

		UUID uuid = UUID.randomUUID(); //java에서 제공하는 UUID 클래스를 이용해 유일한 식별자(서버에 저장되는 파일 이름으로 사용_중복방지를 위함) 얻음
		String uniqueName = uuid.toString().replace("-", "");
		
		File saveFile = new File(uploadDir + "\\" + uniqueName + fileExtension);
		//서버에 저장되는 파일 객체 생성(저장경로, 파일이름, 확장자 사용)
		
		if(!saveFile.exists()) //만일 서버에 파일 저장되는 디렉터리 없으면 새로 만들기
			saveFile.mkdirs();
		
		try { //서버에 파일 저장 후 result를 true로 변경
			file.transferTo(saveFile);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (result) {
			System.out.println("[UploadFileService] FILE UPLOAD SUCCESS!!");
			return uniqueName + fileExtension; //파일 업로드 성공시 파일이름 + 확장자 반환
		} else {
			System.out.println("[UploadFileService] FILE UPLOAD FAIL!!");
			return null;
		}
	}
}
