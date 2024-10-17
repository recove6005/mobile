import 'package:day_record/service/auth_service.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'firebase_options.dart';
import 'login.dart';
import 'navigate/schedule.dart';
import 'navigate/board.dart';
import 'navigate/news.dart';
import 'navigate/profile.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'day_record',
      theme: ThemeData(
          primaryColor: Colors.grey,
      ),
      home: const LoginPage()
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  // 클래스 내부에서만 사용하는 private 변수는 변수명에 _사용
  int _selectedIndex = 0;

  static List<Widget> _pages = <Widget>[
    SchedulePage(),
    BoardPage(),
    NewsPage(),
    ProfilePage()
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(''),),
      drawer: Drawer(
          child: ListView(
            children: <Widget>[
              DrawerHeader(
                  decoration: BoxDecoration(
                    color: Colors.grey,
                  ),
                  child: Center(
                    child: Text(
                      'Settings',
                      style: TextStyle(
                          color: Colors.white,
                          fontSize: 24
                      ),
                    ),
                  )
              ),
              ListTile(
                  leading: Icon(Icons.logout),
                  title: Text('Logout'),
                  onTap: () async {
                    // logout logic
                    AuthService.signOut();
                    Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(builder: (context) => LoginPage())
                    );
                  }
              ),
            ],
          )
      ),
      body: _pages.elementAt(_selectedIndex),
      bottomNavigationBar: BottomNavigationBar(
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(icon: Icon(Icons.note),
              label: 'schedule',
              backgroundColor: Colors.grey),
          BottomNavigationBarItem(icon: Icon(Icons.departure_board),
              label: 'board',
              backgroundColor: Colors.grey),
          BottomNavigationBarItem(icon: Icon(Icons.newspaper),
              label: 'nuews',
              backgroundColor: Colors.grey),
          BottomNavigationBarItem(icon: Icon(Icons.man),
              label: 'profile',
              backgroundColor: Colors.grey),
        ],
        currentIndex: _selectedIndex,
        selectedItemColor: Colors.amber[800],
        onTap: _onItemTapped,
      ),
    );
  }
}