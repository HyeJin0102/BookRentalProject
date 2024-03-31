package com.office.library.book.admin.util;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	//���� ���ε�
	public String upload(MultipartFile file) {
		boolean result = false;
			
		//���� ����
		String fileOriName = file.getOriginalFilename(); //�����ڰ� ���ε��� ���� ���� �̸� ��������
		String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."), fileOriName.length()); //���� ���� Ȯ���� ��������
		String uploadDir = "C:\\HJ_Project\\BookRentalProject\\book_upload\\"; //�������� ������ ����Ǵ� ��ġ ����

		UUID uuid = UUID.randomUUID(); //java���� �����ϴ� UUID Ŭ������ �̿��� ������ �ĺ���(������ ����Ǵ� ���� �̸����� ���_�ߺ������� ����) ����
		String uniqueName = uuid.toString().replace("-", "");
		
		File saveFile = new File(uploadDir + "\\" + uniqueName + fileExtension);
		//������ ����Ǵ� ���� ��ü ����(������, �����̸�, Ȯ���� ���)
		
		if(!saveFile.exists()) //���� ������ ���� ����Ǵ� ���͸� ������ ���� �����
			saveFile.mkdirs();
		
		try { //������ ���� ���� �� result�� true�� ����
			file.transferTo(saveFile);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (result) {
			System.out.println("[UploadFileService] FILE UPLOAD SUCCESS!!");
			return uniqueName + fileExtension; //���� ���ε� ������ �����̸� + Ȯ���� ��ȯ
		} else {
			System.out.println("[UploadFileService] FILE UPLOAD FAIL!!");
			return null;
		}
	}
}
