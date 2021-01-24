## 프로젝트 분할 / 변경
- 기존 **saro commons** 는 **SARO KIT / SARO KIT-EE** 이라는 프로젝트로 분리됩니다.
- **현재 진행하는 프로젝트가 많아 2021년 03월쯤에 정식 버전을 배포할 예정입니다.**
    ### 공통사항
    - JAVA 1.8 -> Kotlin (target jdk 1.8)
        - **자바프로젝트**에서 사용할 수 있게 작성할 예정이며, 타깃 버전은 1.8을 유지합니다.
    - 롬복 제거
        - 호환성에 부담이 되는 롬복을 제거합니다.
    - MAVEN 프로젝트 에서 Gradle 프로젝트로 변경
    ### SARO KIT
    - [https://github.com/saro-lab/kit](https://github.com/saro-lab/kit)
    - 테스트를 위한 junit 을 제외 어떠한 종속성(Dependencies)도 추가하지 않는 순수 프로젝트입니다.
    - 소스 및 결과 파일의 용량이 이슈가 되는 프로젝트에서 사용하기 위한 단순한 유틸입니다.
    ### SARO KIT EE (Enterprise Edition)
    - [https://github.com/saro-lab/kit-ee](https://github.com/saro-lab/kit-ee)
    - SARO KIT을 포함하는 프로젝트로 각종 종속성(Dependencies)이 추가됩니다.
    - 현업에서 주로 사용하는 FTP / EXCEL 등을 쉽게 사용할 수 있게 포함합니다.

## Legacy
- 기존 사용자들을 위해 버그나 개선 등을 계속 받을 예정입니다.
- 버그나 개선사항 등을 아래 레거시 브랜치로 푸시 해주시면 반영하도록 하겠습니다.
- [레거시 브랜치](https://github.com/saro-lab/commons/tree/legacy)
