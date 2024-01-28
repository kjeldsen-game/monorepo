import { User } from "next-auth";

export interface GameUser extends User {
    team: string;
}