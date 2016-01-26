/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.uksw.irc.dto;

/**
 *
 * @author Anna Lukaszek-Zadrozna
 */
public enum UserMode {
    I, // i - marks a users as invisible;
    S,//      s - marks a user for receipt of server notices;
    W, //       w - user receives wallops;
    O //      o - operator flag.
    
}
