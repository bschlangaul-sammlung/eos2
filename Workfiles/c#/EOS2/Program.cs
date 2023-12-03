using System;
using System.Diagnostics;

namespace EOS2
{
    class Program
    {
        static void Main(string[] args)
        {
            LaunchEOS();
        }
        static void LaunchEOS()
        {
            ProcessStartInfo startInfo = new ProcessStartInfo();
            startInfo.CreateNoWindow = true;
            startInfo.UseShellExecute = false;
            startInfo.FileName = "./jre/bin/javaw.exe";
            startInfo.WindowStyle = ProcessWindowStyle.Hidden;
            startInfo.Arguments = "–illegal-access=permit -jar eos.jar";

            try
            {
                using (Process exeProcess = Process.Start(startInfo))
                {
                    exeProcess.WaitForExit();
                }
            } catch {
            }
        }

    }
}
