
class UserInfoModel {
  final String nickname;
  final String email;
  final String intro;

  // 생성자 (required 키워드를 사용하여 필수 값으로 설정)
  UserInfoModel({
    required this.nickname,
    required this.email,
    required this.intro,
  });

  // 클래스 객체를 JSON 데이터로 변환하는 메서드
  Map<String, dynamic> toJson() {
    return {
      'nickname': nickname,
      'email': email,
      'intro': intro,
    };
  }
}