export function InputLabel({
  htmlFor,       // id dell'input
  labelName,     
  type = "text", 
  name,
  value,
  onChange,
  required = true,
  placeholder = "",
  ...rest        // permette di passare altri attributi come className, disabled, min, max ecc.
}) {
 return (
    <div className="">
      <label htmlFor={htmlFor}>{labelName}</label>
      <input
        id={htmlFor}
        type={type}
        name={name}
        value={value}
        onChange={onChange}
        required={required}
        placeholder={placeholder}
        {...rest}
      />
    </div>
  );
}