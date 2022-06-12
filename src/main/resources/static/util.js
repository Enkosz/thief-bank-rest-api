function validate(target, rule) {
  const value = target.val();

  if (value && rule(value)) {
    target.addClass("is-valid");
    target.removeClass("is-invalid");
  } else {
    target.addClass("is-invalid");
    target.removeClass("is-valid");
  }
}

function parseDate(date) {
  const dateParsed = new Date(date);
  return dateParsed.getHours() + ":" +
         dateParsed.getMinutes() + ":" +
         dateParsed.getSeconds() + "; " +
         dateParsed.getDate() + "/" +
         dateParsed.getMonth() + "/" +
         dateParsed.getFullYear();
}
