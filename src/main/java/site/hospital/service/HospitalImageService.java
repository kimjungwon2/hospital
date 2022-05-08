package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.domain.HospitalImage;
import site.hospital.domain.StaffHosInformation;
import site.hospital.repository.StaffHosRepository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HospitalImageService {

    private final StaffHosRepository staffHosRepository;

    public List<HospitalImage> fileInfo(Long staffHosInfoId, List<MultipartFile> multipartFiles) throws Exception{
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosInfoId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        List<HospitalImage> fileList = new ArrayList<>();

        //파일이 비었으면 반환
        if(multipartFiles.isEmpty()){
            return fileList;
        }

        //파일 이름을 업로드 한 날짜로 바꾼다.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String now_date = simpleDateFormat.format(new Date());

        //저장 위치 설정
        String absolutePath = new File("").getAbsolutePath() + "\\";

        String path = "images/" +now_date;
        File file = new File(path);

        //저장 위치의 dir 존재하지 않을 경우 폴더 생성
        if(!file.exists()){
            file.mkdirs();
        }

        for (MultipartFile multipartFile : multipartFiles){
            // 파일이 비어 있지 않을 때 작업을 시작해야 오류가 나지 않는다
            if(!multipartFile.isEmpty()){
                // jpeg, png, gif 파일들만 받아서 처리할 예정
                String contentType = multipartFile.getContentType();
                String originalFileExtension;

                // 확장자 명이 없으면 증지.
                if (ObjectUtils.isEmpty(contentType))break;
                else
                {
                    if(contentType.contains("image/jpeg")){
                        originalFileExtension = ".jpg";
                    }
                    else if(contentType.contains("image/png")){
                        originalFileExtension = ".png";
                    }
                    else if(contentType.contains("image/gif")){
                        originalFileExtension = ".gif";
                    }
                    // 다른 파일 명이면 아무 일 하지 않는다
                    else break;
                }

                // 각 이름은 겹치면 안되므로 나노 초까지 동원하여 지정
                String new_file_name = Long.toString(System.nanoTime()) + originalFileExtension;
                // 생성 후 리스트에 추가
                HospitalImage hospitalImage = HospitalImage.builder()
                        .staffHosInformation(staffHosInformation)
                        .fileSize(multipartFile.getSize())
                        .filePath(path+"/"+new_file_name)
                        .fileName(multipartFile.getOriginalFilename())
                        .build();

                fileList.add(hospitalImage);

                // 저장된 파일로 변경하여 이를 보여주기 위함
                file = new File(absolutePath + path + "/" + new_file_name);
                multipartFile.transferTo(file);
            }
        }
        return fileList;
    }
}
