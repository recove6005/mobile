import 'dart:math';
import 'dart:developer' as dev;
import 'package:firebase_auth/firebase_auth.dart';
import 'package:mailer/mailer.dart';
import 'package:mailer/smtp_server/gmail.dart';

class AuthService {
  static final FirebaseAuth _auth = FirebaseAuth.instance;
  static String _authCode = '';

  static bool isLogined() {
    User? user = _auth.currentUser;
    if(user != null) return true;
    else return false;
  }

  // create
  static Future<bool?> createUser(String email, String password) async {
      try {
          await _auth.createUserWithEmailAndPassword(
          email: email,
          password: password,
        );
          dev.log('User account is created.');
          return true;
      } on FirebaseAuthException catch (e) {
        if (e.code == 'weak-password') {
          dev.log('The password provided is too weak.', name: 'day_record_log');
          return false;
        } else if (e.code == 'email-already-in-use') {

          dev.log('The account already exists for that email.', name: 'day_record_log');
          return false;
        }
      } catch (e) {
        dev.log('$e', name: 'day_record_log');
          return false;
      }
  }

  // 랜덤한 6자리 숫자 문자열 생성
  static String _generateRandomNumberString() {
    final Random random = Random();
    String randomNumber = '';
    for(int i = 0; i < 6; i++) {
      randomNumber += random.nextInt(10).toString();
    }
    return randomNumber;
  }

  // email authentification
  static Future<String?> sendEmailCode(email) async {
    _authCode = _generateRandomNumberString();
    String hostId = 'leehan6005@gmail.com';
    String hostPw = 'ilfmgzgsbkawbakg';
    final smtpServer = gmail(hostId, hostPw);
    final message = Message()
    ..from = Address(hostId, 'day_record')
    ..recipients.add(email)
    ..subject = 'day_record : Verification Code'
    ..text = 'Your email verification code is $_authCode.';

    try {
      final sendReport = await send(message, smtpServer);
      dev.log('Email sent: ${sendReport.toString()}');
      return _authCode;
    } catch (e) {
      dev.log('Failed to send verification email: $e', name: "day_record_log");
      return null;
    }
  }


  // login
  static Future<bool> signInwithEmail(String email, String password) async {
    try {
      UserCredential userCredential = await _auth.signInWithEmailAndPassword(
          email: email,
          password: password
      );
      dev.log('Logind', name: 'day_record_log');
      return true;
    } catch (e) {
      dev.log('Login failed: $e', name: 'day_record_log');
      return false;
    }
  }

  // logout
  static Future<void> signOut() async {
    await _auth.signOut();
  }
}