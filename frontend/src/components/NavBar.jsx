import { VscAccount } from "react-icons/vsc";
import { GiWeightLiftingUp } from "react-icons/gi";
import { CiChat1 } from "react-icons/ci";
import { LuLogOut } from "react-icons/lu";
import { AccountInfo } from "./AccountInfo";




function NavBar() {
    
    return (
        <div
            className="bg-white h-16 justify-center flex items-center mr-12 ml-12 rounded-md shadow-xl/30 opacity-85 "
        >
            <ul
                className="flex space-x-70 font-serif font-[1000] italic text-[16px]"
            >
                <li><a href="/login"
                    className="flex items-center gap-2 hover:underline hover:decoration-blue-500"
                    onClick={() => {localStorage.removeItem("token")}}>
                    <LuLogOut />
                    Logout
                </a></li>
                <li><a href=""
                    className="flex items-center gap-2 hover:underline hover:decoration-blue-500">
                    <CiChat1 />
                    Chat
                </a></li>
                <li><a href=""
                    className="flex items-center gap-2 hover:underline hover:decoration-blue-500">
                    <GiWeightLiftingUp />
                    Training
                </a></li>
                <li><a href=""
                    className="flex items-center gap-2 hover:underline hover:decoration-blue-500"
                    onClick={() => <AccountInfo />}>
                    <VscAccount />
                    Account
                </a></li>
            </ul>
        </div>
    )
}

export default NavBar