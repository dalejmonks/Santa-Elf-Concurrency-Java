/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santaElf;

/**
 *
 * @author Dallus
 */
public class Gift {
    String name;
    boolean forMale;
    public Gift(String name, boolean forMale) {
        this.name = name;
    }
    
    public boolean forMale(){
        return forMale;
    }

    @Override
    public String toString() {
        return "Gift{" + "name=" + name + ", wrapped in " + (forMale? "blue" : "pink") +" wrapping paper" +'}';
    }
}
