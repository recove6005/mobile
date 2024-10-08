import 'package:flutter/cupertino.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'service/auth_service.dart';

void main() => runApp(LoginPage());

class LoginPage extends StatelessWidget {
  const LoginPage({Key? key}): super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'login_home',
      theme: ThemeData(
        primaryColor: Colors.grey,
      ),
      home: const LoginHome(),
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Text('login home'),
    );
  }
}