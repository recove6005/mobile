package com.lpennavic.generalboard.Sender

import android.util.Log
import java.io.UnsupportedEncodingException
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailSender {
    companion object {
        private const val SMTP_HOST = "smtp.gmail.com"
        private const val SMTP_PORT = "587"
        private const val USERNAME ="leehan6005@gmail.com"
        private const val APP_PASSWORD ="atcmuvfjpephzgjw"
    }

    fun sendEmail(recipientEmailAddress: String, subject: String, content: String) {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")

            //javax.net.ssl.SSLHandshakeException: No enabled protocols; TLSv1 에러 >>
            put("mail.smtp.ssl.protocols", "TLSv1.2") // Java 8 이상에서 TLS 1.2를 강제 활성화
            // props.put("mail.smtp.starttls.enable", "true"); // TLS 설정 SMTP 포트: 587
            // props.put("mail.smtp.ssl.enable", "true"); // SSL 설정 SMTP 포트: 465

            put("mail.smtp.host", SMTP_HOST)
            put("mail.smtp.port", SMTP_PORT)
        }

        val session = Session.getInstance(props, object: javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(USERNAME, APP_PASSWORD)
            }
        })

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(USERNAME))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmailAddress))
            message.setSubject(subject)
            message.setText(content)

            Transport.send(message)
            Log.v("emailsender", "email sended.")
        } catch (e: AddressException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }
}


// gradle.properties 에
// org.gradle.jvmargs=-Xmx4608m -Dfile.encoding=UTF-8
// 문구 추가
// A failure occurred while executing com.android.build.gradle.internal.tasks.MergeJavaResWorkAction
// 오류 해결