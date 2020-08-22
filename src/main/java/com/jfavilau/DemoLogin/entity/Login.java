package com.jfavilau.demologin.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author jhon.avila
 * 
 * Objeto Login con sus respectivos atributos, algunos de estos son replica de 
 * los usuarios que se crean con el aplicativo  por el administrador y/o los
 * gestores
 */

@Entity
public class Login implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String user;
    private String password;
    private String password2;
    private String password3;
    private Timestamp dateCreatePass;
    private Timestamp dateLastSignIn;
    private String email;
    private String flag;
    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String question5;
    private String reply1;
    private String reply2;
    private String reply3;
    private String reply4;
    private String reply5;
    private String active;
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() 
    {
        return id;
    }

    public void setId ( Long id ) 
    {
        this.id = id;
    }

    @Basic
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Basic
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    @Basic
    public String getPassword3() {
        return password3;
    }

    public void setPassword3(String password3) {
        this.password3 = password3;
    }

    @Basic
    public Timestamp getDateCreatePass() {
        return dateCreatePass;
    }

    public void setDateCreatePass(Timestamp dateCreatePass) {
        this.dateCreatePass = dateCreatePass;
    }

    @Basic
    public Timestamp getDateLastSignIn() {
        return dateLastSignIn;
    }

    public void setDateLastSignIn(Timestamp dateLastSignIn) {
        this.dateLastSignIn = dateLastSignIn;
    }

    @Basic
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Basic
    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    @Basic
    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    @Basic
    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    @Basic
    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    @Basic
    public String getQuestion5() {
        return question5;
    }

    public void setQuestion5(String question5) {
        this.question5 = question5;
    }

    @Basic
    public String getReply1() {
        return reply1;
    }

    public void setReply1(String reply1) {
        this.reply1 = reply1;
    }

    @Basic
    public String getReply2() {
        return reply2;
    }

    public void setReply2(String reply2) {
        this.reply2 = reply2;
    }


    @Basic
    public String getReply3() {
        return reply3;
    }

    public void setReply3(String reply3) {
        this.reply3 = reply3;
    }


    @Basic
    public String getReply4() {
        return reply4;
    }

    public void setReply4(String reply4) {
        this.reply4 = reply4;
    }


    @Basic
    public String getReply5() {
        return reply5;
    }

    public void setReply5(String reply5) {
        this.reply5 = reply5;
    }


    @Basic
    public String getActive() {
        return active;
    }


    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login)) return false;

        Login login = (Login) o;

        return getId().equals(login.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }


    @Override
    public String toString() {
        return "Login{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                ", password3='" + password3 + '\'' +
                ", dateCreatePass=" + dateCreatePass +
                ", dateLastSignIn=" + dateLastSignIn +
                ", email='" + email + '\'' +
                ", flag='" + flag + '\'' +
                ", question1='" + question1 + '\'' +
                ", question2='" + question2 + '\'' +
                ", question3='" + question3 + '\'' +
                ", question4='" + question4 + '\'' +
                ", question5='" + question5 + '\'' +
                ", reply1='" + reply1 + '\'' +
                ", reply2='" + reply2 + '\'' +
                ", reply3='" + reply3 + '\'' +
                ", reply4='" + reply4 + '\'' +
                ", reply5='" + reply5 + '\'' +
                ", active='" + active + '\'' +
                '}';
    }

}
