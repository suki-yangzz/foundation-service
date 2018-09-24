package net.susss.demo.foundationservice.protocol;

import net.susss.demo.foundationservice.protocol.proto.AddressBookProtos.Person;
import net.susss.demo.foundationservice.protocol.proto.AddressBookProtos.AddressBook;

import java.io.FileInputStream;

public class Consumer {
    // Iterates though all people in the AddressBook and prints info about them.
    static void Print(AddressBook addressBook) {
        for (Person person: addressBook.getPeopleList()) {
            System.out.println("Person ID: " + person.getId());
            System.out.println("  Name: " + person.getName());
            if (person.hasEmail()) {
                System.out.println("  E-mail address: " + person.getEmail());
            }

            for (Person.PhoneNumber phoneNumber : person.getPhonesList()) {
                switch (phoneNumber.getType()) {
                    case MOBILE:
                        System.out.print("  Mobile phone #: ");
                        break;
                    case HOME:
                        System.out.print("  Home phone #: ");
                        break;
                    case WORK:
                        System.out.print("  Work phone #: ");
                        break;
                }
                System.out.println(phoneNumber.getNumber());
            }
        }
    }

    // Main function:  Reads the entire address book from a file and prints all
    //   the information inside.
    public static void main(String[] args) throws Exception {

        // Read the existing address book.
        AddressBook addressBook =
                AddressBook.parseFrom(new FileInputStream("addressbook.data"));

        Print(addressBook);
    }
}
