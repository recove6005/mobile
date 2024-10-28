import 'package:flutter/material.dart';
import 'package:logger/logger.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'board',
      home: Scaffold(
        appBar: AppBar(title: const Text('토론')),
        body: const BoardPage(),
      ),
    );
  }
}

class BoardPage extends StatefulWidget {
  const BoardPage({super.key});

  @override
  State<BoardPage> createState() => _BoardPage();
}

class _BoardPage extends State<BoardPage> {
  static var logger = Logger();

  static String title = "title";
  static String date = "date";
  static String content = "content";
  static int itemCount = 20;

  static void _addPoster() {
    logger.d('day_record_log : Adding poster.');
  }

  @override
  void initState() {
    super.initState();

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('토론'),),
      body: SizedBox.expand(
        child: Container(
          margin: const EdgeInsets.all(20),
          child: CustomScrollView(
                slivers: [
                  SliverList(
                      delegate: SliverChildBuilderDelegate(
                          (context, index) {
                            return Container(
                              margin: const EdgeInsets.all(12),
                              child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Row(
                                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                      children: [
                                        Text(title,
                                        maxLines: 1,
                                        overflow: TextOverflow.ellipsis,
                                        style: const TextStyle(
                                          fontSize: 20,
                                          fontWeight: FontWeight.bold
                                          ),
                                        ),
                                        Text(date,
                                        style: const TextStyle(
                                          fontSize: 15,
                                          color: Colors.grey
                                          ),
                                        )
                                      ],
                                    ),
                                    Text(content,
                                      maxLines: 1,
                                      overflow: TextOverflow.ellipsis,
                                      style: const TextStyle(fontSize: 18,),)
                                  ],
                                )
                            );
                          },
                        childCount: itemCount
                      )
                  ),
                ],
          ),
        ),
      ),
      floatingActionButton: const FloatingActionButton(
        onPressed: _addPoster,
        child: Icon(Icons.add)
      ),
    );
  }
}