import 'dart:developer' as dev;
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:day_record/model/UserModel.dart';

class UserRepository {
  static Future<bool> insertUser(String nickname, String email) async {
    UserInfoModel newUser = UserInfoModel(
        nickname: nickname,
        email: email,
        intro: ""
    );

    try {
      FirebaseFirestore _firestore = FirebaseFirestore.instance;
      await _firestore.collection('user_info').doc(email).set(newUser.toJson());
      dev.log('User added to store: $newUser', name: 'day_record_log');
    } catch (e) {
      dev.log('Failed to add user: $e', name: 'day_record_log');
      return false;
    }

    return true;
  }

}