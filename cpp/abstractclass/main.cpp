#include "IWriteData.h"
#include "ConsoleWriteData.h"

int main(int argc, const char * argv[])
{
    ConsoleWriteData cwd = ConsoleWriteData();
    IWriteData& r_iwd = cwd;
    IWriteData* p_iwd = &cwd;
    
    cwd.WriteLine(10);
    r_iwd.WriteLine(14.6);
    p_iwd->WriteLine(L"Hello Abstract World!");
    return 0;
}
