export function InputLabel({
  htmlFor,       // id dell'input
  labelName,
  type = "text",
  name,
  value,
  onChange,
  required = true,
  placeholder = "",
  labelWidth = "w-1/3",
  inputWidth = "w-2/3",
  ...rest        // permette di passare altri attributi come className, disabled, min, max ecc.
}) {
  return (
    <div className="flex items-center ">
      <label
        className={`${labelWidth} text-gray-700 font-semibold`}
        htmlFor={htmlFor}>{labelName}</label>
      <input
        id={htmlFor}
        type={type}
        name={name}
        value={value}
        onChange={onChange}
        required={required}
        placeholder={placeholder}
        className={`${type === "checkbox" ? "mr-2": inputWidth} border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-400 transition`}
        {...rest}
      />
    </div>
  );
}