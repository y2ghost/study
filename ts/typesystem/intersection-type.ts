// 一个intersection type类型就是多个类型合为一个
interface Person {
  name: string;
}

interface Contact {
  phone: string;
}

function showPersonContact(personContact: Person & Contact): void {
  console.log(personContact);
}

let personContact: Person & Contact = { name: "YY", phone: "111-111-111" };
showPersonContact(personContact);

// 可以用于接口或是类
interface PersonDetail {
  detail: Person & Contact;
}

let personDetail: PersonDetail = {
  detail: { name: "TT", phone: "111-111-111" },
};
console.log(personDetail);
