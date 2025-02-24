-- <ConfigZone>
local url = "http://localhost:8080/postBlockData"
local throttle = 3
local lastSuccess = false
-- </ConfigZone>

if not http then
    print("HTTP API is not enabled.")
    return
end
local itteration = 0
local intResets = 0
local proccessedItems =  0
local lastItemCount = 0
local data
local blockData
while (true) do
    term.write("("..itteration..") ")
    if (turtle.suckUp()) then
        --print("[SUCK] Success.")
        lastItemCount = turtle.getItemCount()
        blockData = turtle.getItemDetail()
        -- Data to send in the POST request
        data = {
            itemName = blockData.name,
            itemAmount = blockData.count,
            itemDamage = blockData.damage
        }
        --print("[ITEM-DETAILS] count: "..blockData.count)
        --print("[ITEM-DETAILS] name: "..blockData.name)
        --print("[ITEM-DETAILS] damage: "..blockData.damage)
        --print(data)
        local json = textutils.serializeJSON(data)
        local success = false
        while (not success) do
            local response, error = http.post(url, json, {
                ["Content-Type"] = "application/json" -- Set the correct content type
            })
            if response then
                success = true
                --print("Response code:", response.getResponseCode())
                --print("Response body:")
                --print(response.readAll())
                if (response.getResponseCode() == 200) then
                    print("[SCAN-"..throttle.."] "..blockData.count.." x "..blockData.name.." -> "..response.getResponseCode())
                    proccessedItems = proccessedItems + 1
                    turtle.dropDown()
                    lastSuccess = true
                end
                response.close()
            else
                print("[HTTP]:", error)
            end
            os.sleep(throttle)
        end
    else
        print("[STANDBY]")
        lastSuccess = false
    end
    if lastSuccess then
        if throttle <= 0.4 then
            throttle = 0.4
        else
            if lastItemCount >= 3 then
                throttle = throttle - 0.2
            else
                if throttle >= 10 then
                    throttle = 10
                else
                    if blockData.name ~= "minecraft:cobblestone" then
                        throttle = throttle + 0.2
                    end
                end
            end
        end
    else
        if throttle >= 10 then
            throttle = 10
        else
            throttle = throttle + 0.2
        end
    end
    os.sleep(throttle);
    if (itteration < 300000) then
        itteration = itteration + 1
    else
        itteration = 0
        intResets = intResets + 1
    end

end