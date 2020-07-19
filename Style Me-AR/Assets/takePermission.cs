using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.Android;

public class takePermission : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {

        if (!Permission.HasUserAuthorizedPermission(Permission.ExternalStorageWrite))
        {
            Permission.RequestUserPermission(Permission.ExternalStorageWrite);
        }
        if (!Permission.HasUserAuthorizedPermission(Permission.ExternalStorageRead))
        {
            Permission.RequestUserPermission(Permission.ExternalStorageRead);
        }
    }

    // Update is called once per frame
    void Update()
    {
        if (Permission.HasUserAuthorizedPermission(Permission.ExternalStorageWrite))
        {
            SceneManager.LoadScene("FrontAugmentation");
        }
        else {
            Permission.RequestUserPermission(Permission.ExternalStorageWrite);
        }
    }
}
