using GoogleARCore;
using System.Collections.Generic;
using UnityEngine;
public class ARController : MonoBehaviour
{
    // we will fill this list with planes that arcore detected in the current frame
    private List<TrackedPlane> m_NewTrackedPlanes = new List<TrackedPlane>();
    public GameObject GridPrefab;
    public GameObject outfit;
    public GameObject ARCamera;
    GameObject grid;
    Anchor anchor;
    // Start is called before the first frame update
    void Start()
    {
        outfit.SetActive(false);
    }

    // Update is called once per frame
    bool flag = true;


    private void Update()
    {
        if (Session.Status != SessionStatus.Tracking)
        {
            return;
        }
        Session.GetTrackables<TrackedPlane>(m_NewTrackedPlanes, TrackableQueryFilter.New);
        for (int i = 0; i < m_NewTrackedPlanes.Count; ++i)
        {
            grid = Instantiate(GridPrefab, Vector3.zero, Quaternion.identity, transform);
            grid.GetComponent<GridVisualiser>().Initialize(m_NewTrackedPlanes[i]);
        }
        

    }
}

