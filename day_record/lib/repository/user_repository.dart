import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:day_record/model/UserModel.dart';
import 'package:logger/logger.dart';

class UserRepository {
  static var logger = Logger();

  static Future<bool> insertUser(String nickname, String email) async {
    UserInfoModel newUser = UserInfoModel(
        nickname: nickname,
        email: email,
        intro: ""
    );

    logger.d('day_record_log : User model is created => $newUser');

    try {
      FirebaseFirestore _firestore = FirebaseFirestore.instance;
      await _firestore.collection('user_info').doc(email).set(newUser.toJson());
      logger.d('day_record_log : User added to store $newUser');
    } catch (e) {
      logger.e('day_record_log : Failed to add user $e');
      return false;
    }
    return true;
  }
}