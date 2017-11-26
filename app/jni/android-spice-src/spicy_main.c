int spice_main(char* cmd);
int main()
{
    return spice_main("./spicy -h 192.168.1.104 -p 5900");
}

