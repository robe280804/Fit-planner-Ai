import { use, useContext, useEffect, useRef, useState } from "react";
import { AuthContext } from "../contexts/AuthContext";

export function AccountInfo() {
    const { user } = useContext(AuthContext)
    const [open, setOpen] = useState(true)
   

    return (
        <>
        {open && (
            <div>
            </div>
        )}
        </>
    )
}