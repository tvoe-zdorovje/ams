fn process_request(request) {
    let claims = request.context["apollo::authentication::jwt_claims"];
    let policies = request.context["apollo::authorization::required_policies"];

    if policies != () {
        for key in policies.keys() {
            let array = key.split(":");
            if array.len == 2 {
                // todo
                switch array[0] {
                    "permission" => {
                        policies[key] = claims[`permission`] == array[1];
                    }
                    "roles" => {
                        policies[key] = claims[`roles`].contains(array[1]);
                    }
                    _ => {}
                }
            }
        }
    }
    request.context["apollo::authorization::required_policies"] = policies;
}
