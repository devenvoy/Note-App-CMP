import UIKit
import SwiftUI
import ComposeApp
import FirebaseRemoteConfig


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let hostVC = UIViewController()
        hostVC.view.backgroundColor = .white

        let remoteConfig = RemoteConfig.remoteConfig()
        let settings = RemoteConfigSettings()
        settings.minimumFetchInterval = 10000
        remoteConfig.configSettings = settings

         remoteConfig.fetchAndActivate { status, error in
                if let error = error {
                    print("Remote Config fetch failed: \(error.localizedDescription)")
                }

                let isForceUpdateEnabled = remoteConfig.configValue(forKey:"test_config").boolValue

                DispatchQueue.main.async {

                    let mainVC = MainViewControllerKt.MainViewController(
                      testConfig: isForceUpdateEnabled
                    )

                    hostVC.addChild(mainVC)
                    mainVC.view.frame = hostVC.view.bounds
                    mainVC.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
                    hostVC.view.addSubview(mainVC.view)
                    mainVC.didMove(toParent: hostVC)
                }
            }

           return hostVC

//         MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}



