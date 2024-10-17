import 'package:day_record/main.dart';
import 'package:day_record/register.dart';
import 'package:flutter/cupertino.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'service/auth_service.dart';

 void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Day Record',
      home: LoginPage(),
    );
  }
}

class LoginPage extends StatelessWidget {
  const LoginPage({Key? key}): super(key: key);

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
                    'Login',
                    style: TextStyle(fontSize: 30, fontWeight: FontWeight.bold),
                  )
              )
          ),
          Expanded(child: LoginHome(),)
        ]
      ),
    );
  }
}

class LoginHome extends StatefulWidget {
  const LoginHome({Key? key}) : super(key: key);
  static final FirebaseAuth _auth = FirebaseAuth.instance;

  @override
  State<LoginHome> createState() => _LoginHome();
}

class _LoginHome extends State<LoginHome> {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  @override
  void initState() {
    super.initState();
    // 자동로그인
    if(AuthService.isLogined()) {
      Navigator.pushReplacement(
          context as BuildContext,
          MaterialPageRoute(
              builder: (context) => MyHomePage()
          )
      );
    }
  }

  void _login() async {
    String email = _emailController.text;
    String password = _passwordController.text;

    Future<bool> logind = AuthService.signInwithEmail(email, password);
    if(await logind) {
      // 로그인 성공
      Navigator.pushReplacement(
          context as BuildContext,
          MaterialPageRoute(builder: (context) => MyHomePage())
      );
    } else {
      // 로그인 실패

    }
  }

  void _register() async {
    // 회원 가입 페이지로 이동
    Navigator.push(
      context as BuildContext,
      MaterialPageRoute(builder: (context) => RegisterPage())
    );
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
                  controller: _emailController,
                  decoration: InputDecoration(
                      labelText: 'email',
                      border: OutlineInputBorder()
                  ),
                ),
                SizedBox(height: 16),
                TextField(
                  controller: _passwordController,
                  decoration: InputDecoration(
                      labelText: 'password',
                      border: OutlineInputBorder()
                  ),
                  obscureText: true, // 비밀번호 숨기기
                ),
                SizedBox(height: 40),
                Container(
                  width: double.infinity,
                  child: ElevatedButton(
                      onPressed: _login,
                      child: Text('Sign in')
                  ),
                ),
                SizedBox(height: 16),
                Container(
                  width: double.infinity,
                  child: ElevatedButton(
                      onPressed: _register,
                      child: Text('Sign up')
                  ),
                )
              ],
            )
        )
    );
  }
}