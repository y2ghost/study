console.log(this);

const calcAge = function (birthYear) {
  console.log(2037 - birthYear);
  console.log(this);
};
calcAge(1991);

const calcAgeArrow = birthYear => {
  console.log(2037 - birthYear);
  console.log(this);
};
calcAgeArrow(1980);

const ydoit = {
  year: 1991,
  calcAge: function () {
    console.log(this);
    console.log(2037 - this.year);
  },
};
ydoit.calcAge();

const matilda = {
  year: 2017,
};

matilda.calcAge = ydoit.calcAge;
matilda.calcAge();

const f = ydoit.calcAge;
f();
