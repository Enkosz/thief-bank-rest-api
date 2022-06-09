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
