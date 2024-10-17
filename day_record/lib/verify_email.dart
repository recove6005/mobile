import 'package:day_record/main.dart';
import 'package:day_record/repository/user_repository.dart';
import 'package:day_record/service/auth_service.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'dart:developer' as dev;


void main() {
  String nickname = '';
  String email = '';
  String password = '';
  return runApp(MyApp(nickname: nickname, email: email, password: password,));
}

class MyApp extends StatelessWidget {
  final String nickname;
  final String email;
  final String password;

  const MyApp({
    required this.nickname,
    required this.email,
    required this.password,
  });

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '',
      home: VerifyPage(
        nickname: nickname,
        email: email,
        password: password,
      ),
    );
  }
}

class VerifyPage extends StatelessWidget {
  final String nickname;
  final String email;
  final String password;

  const VerifyPage({
    required this.nickname,
    required this.email,
    required this.password,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(''),
      ),
      body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
                margin: EdgeInsets.all(16),
                height: 60,
                width: double.infinity,
                child: Center(
                    child: Text(
                      'day_record',
                      style: TextStyle(fontSize: 50, fontWeight: FontWeight.bold),
                    )
                )
            ),
            Container(
                height: 45,
                width: double.infinity,
                child: Center(
                    child: Text(
                      'Sign up',
                      style: TextStyle(fontSize: 30, fontWeight: FontWeight.bold),
                    )
                )
            ),
            Expanded(child: VerifyHome(
                nickname: nickname,
                email: email,
                password: password,
              ),
            ),
          ],
      ),
    );
  }
}

class VerifyHome extends StatefulWidget {
  final String nickname;
  final String email;
  final String password;

  const VerifyHome({
    required this.nickname,
    required this.email,
    required this.password,
  });

  @override
  State<VerifyHome> createState() => _VerifyEmail();
}

class _VerifyEmail extends State<VerifyHome> {
  late String nickname;
  late String email;
  late String password;

  final TextEditingController _codeController = TextEditingController();
  bool _codeCheck = false;
  String? _authCode = '';

  get developer => null;

  void initState() {
    super.initState();
    nickname = widget.nickname; // widget을 통해 초기화
    email = widget.email;
    password = widget.password;
  }

  static void _showToast(msg) {
    Fluttertoast.showToast(
      msg: msg,
      toastLength: Toast.LENGTH_SHORT,
      gravity: ToastGravity.BOTTOM,
      timeInSecForIosWeb: 1,
      backgroundColor: Colors.black54,
      textColor: Colors.white,
      fontSize: 16.0
    );
  }

  void _sendCode() async {
    _authCode = await AuthService.sendEmailCode(email);
    // 이메일 코드 전송
    if(_authCode != null) {
      // 이메일 코드 전송 성공
      dev.log('Verification code is sented: $email', name: 'day_record_log');
      _showToast('Verification code is sented.');
    } else {
      // 이메일 코드 전송 실패
      dev.log('VFailed to send verification code: $email', name: 'day_record_log');
      _showToast('Failed to send verification code.');
    }
  }

  void _signUp() {
    // 코드 인증 성공
    if(_codeController.text == _authCode) {
      // 계정 생성
      bool actCreateCheck = AuthService.createUser(email, password) as bool;

      // 유저 생성
      bool userCreateCheck = UserRepository.insertUser(nickname, email) as bool;

      if(actCreateCheck && userCreateCheck) {
        // 로그인
        Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (context) => MyHomePage())
        );
      }
    } else {
      _codeCheck = true;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
        padding: EdgeInsets.symmetric(horizontal: 100),
        height: double.infinity,
        width: double.infinity,
        child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                TextField(
                  controller: _codeController,
                  decoration: InputDecoration(
                      labelText: 'code number',
                      border: OutlineInputBorder()
                  ),
                ),
                SizedBox(height: 16),
                Container(
                  width: double.infinity,
                  child: ElevatedButton(
                      onPressed: _sendCode,
                      child: Text('Sending code number')
                  ),
                ),
                SizedBox(height: 16),
                Container(
                  width: double.infinity,
                  child: ElevatedButton(
                      onPressed: _signUp,
                      child: Text('Sign up')
                  ),
                ),
                if(_codeCheck)
                Text(
                    'Please check your code number',
                    style: TextStyle(color: Colors.red)
                ),
              ],
            )
        )
    );
  }
}