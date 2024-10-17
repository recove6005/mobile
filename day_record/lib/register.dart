import 'package:day_record/verify_email.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

void main() {
  return runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Day Record',
      home: RegisterPage(),
    );
  }
}

class RegisterPage extends StatelessWidget {
  const RegisterPage({Key? key}) : super(key: key);

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
            Expanded(child: RegisterHome(),)
          ]
      ),
    );
  }
}

class RegisterHome extends StatefulWidget {
  const RegisterHome({Key? key}) : super(key: key);

  @override
  State<RegisterHome> createState() => _RegisterHome();
}

class _RegisterHome extends State<RegisterHome> {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _nicknameController = TextEditingController();

  void _createUser() {
      String email = _emailController.text;
      String password = _passwordController.text;
      String nickname = _nicknameController.text;

      Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => VerifyPage(
            nickname: nickname,
            email: email,
            password: password)
          )
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
                  controller: _nicknameController,
                  decoration: InputDecoration(
                      labelText: 'nickname',
                      border: OutlineInputBorder()
                  ),
                ),
                SizedBox(height: 16),
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
                      onPressed: _createUser,
                      child: Text('Next')
                  ),
                ),
              ],
            )
        )
    );
  }

}